package br.com.poccore.dao.impl;

import br.com.poc.occ.dto.product.PocProductReviewsInfoData;
import br.com.poccore.dao.PocProductDao;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.temporal.Temporal;
import java.util.*;
import java.time.Duration;
import java.util.stream.Collectors;


public class DefaultPocProductDao implements PocProductDao {

    private FlexibleSearchService flexibleSearchService;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){

         PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData = new PocProductEngagementSummaryInfoData();

         //média de rating
        double rating = 0.0;
        int ratingAVG  = getCountRatingAVG(productCode, true);
        int ratingAVGTotal  = getCountRatingAVG(productCode, false);
        if(ratingAVGTotal > 0) {
            rating = (((double) ratingAVG / ratingAVGTotal) * 100);
        }

        DecimalFormat df = new DecimalFormat("0.00"); // Use "0.00" to ensure two digits
        df.setRoundingMode(RoundingMode.HALF_UP); // Set rounding behavior
        String roundedValue = df.format(rating);

        pocProductEngagementSummaryInfoData.setRating(roundedValue);
        //% Verified
        double verified = 0.0;
        int verifiedAVG  = getCountVerified(productCode, true);
        int verifiedAVGTotal  = getCountVerified(productCode, false);
        if(verifiedAVGTotal > 0) {
            verified = (((double) verifiedAVG / verifiedAVGTotal) * 100);
        }

        DecimalFormat df1 = new DecimalFormat("0.00"); // Use "0.00" to ensure two digits
        df1.setRoundingMode(RoundingMode.HALF_UP); // Set rounding behavior
        String roundedValue2 = df.format(verified);

        pocProductEngagementSummaryInfoData.setVerified(roundedValue2);

        //% top N reviews úteis
        SortedMap<String, Integer> listNReviews = getTopNRating(productCode);
        List<PocProductReviewsInfoData> toplist = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listNReviews.entrySet()) {
            PocProductReviewsInfoData pocProductReviewsInfoData = new PocProductReviewsInfoData();
            pocProductReviewsInfoData.setHeadLine(entry.getKey());
            pocProductReviewsInfoData.setReviewCount(entry.getValue());
            toplist.add(pocProductReviewsInfoData);
        }
        pocProductEngagementSummaryInfoData.setTopReviews(toplist);

       //  Contagem de perguntas - somar o total de inquiry com approved para o produto
        int TotalQuestions  = getTotalQuestions(productCode);
        pocProductEngagementSummaryInfoData.setQuestionCount(TotalQuestions);


        //Taxa de publicação - CustomerReview
        double publication = 0.0;
        int publicationAVGTotal  = getCountPublication(productCode, true);
        int publicationAVG  = getCountPublication(productCode, false);
        if(publicationAVGTotal > 0) {
            publication = (((double) publicationAVG / publicationAVGTotal) * 100);
        }

        DecimalFormat df3 = new DecimalFormat("0.00"); // Use "0.00" to ensure two digits
        df3.setRoundingMode(RoundingMode.HALF_UP); // Set rounding behavior
        String roundedValue3 = df.format(publication);

        pocProductEngagementSummaryInfoData.setPublication(roundedValue3);

        int responseTime  = getResponseTime(productCode);
        pocProductEngagementSummaryInfoData.setResponseTime(responseTime);

        return  pocProductEngagementSummaryInfoData;
    }


    private int getCountRatingAVG(String productCode, boolean isUseful) {

        String queryString = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }";


        StringBuilder query = new StringBuilder(queryString);

        if(isUseful){
            query.append(" WHERE {pr.code} = ?code and {crr.isUseful} = 1");
        } else{
            query.append(" WHERE {pr.code} = ?code and {crr.isUseful} IS NOT NULL");
        }

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("code", productCode);
        fQuery.setResultClassList(Collections.singletonList(Integer.class));

        SearchResult<Integer> result = flexibleSearchService.search(fQuery);
        return result.getResult().getFirst();

    }

    private int getCountVerified(String productCode, boolean hasBoughtProduct) {

        String queryString = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }";

        StringBuilder query = new StringBuilder(queryString);

        if(hasBoughtProduct){
            query.append(" WHERE {pr.code} = ?code and {cr.hasBoughtProduct} = 1");
        } else{
            query.append(" WHERE {pr.code} = ?code and {cr.hasBoughtProduct} IS NOT NULL");
        }

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("code", productCode);
        fQuery.setResultClassList(Collections.singletonList(Integer.class));

        SearchResult<Integer> result = flexibleSearchService.search(fQuery);
        return result.getResult().getFirst();

    }

    private SortedMap<String, Integer> getTopNRating(String productCode) {

        String queryString = "SELECT {cr.headline}, count({crr.isUseful})" +
                " FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     } " +
                " WHERE {pr.code} = ?code and {crr.isUseful} = 1 and  {cr.hasBoughtProduct} = 1" +
                " GROUP BY {cr.headline}";

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Arrays.asList(String.class, Integer.class));
        query.addQueryParameter("code", productCode);
        final SearchResult<List<Object>> result = flexibleSearchService.search(query);
        Map<String, Integer> couponRedemptionsMap;

        couponRedemptionsMap = result.getResult().stream()
                .collect(Collectors.toMap(c -> (String) c.getFirst(), c -> (Integer) c.get(1)));

        SortedMap<String, Integer> couponRedemptionsMapSorted = new TreeMap<>(Comparator.reverseOrder());

        couponRedemptionsMapSorted.putAll(couponRedemptionsMap);

        for (Map.Entry<String, Integer> entry : couponRedemptionsMapSorted.entrySet()) {
            if(entry.getValue().equals(0)){
                couponRedemptionsMapSorted.remove(entry.getKey(),entry.getValue());
            }
        }

        return couponRedemptionsMapSorted;

    }

    private int getTotalQuestions(String productCode) {

        String queryString = "SELECT COUNT({cpi.pk})" +
                "FROM {" +
                "                CustomerProductInquiry as cpi" +
                "      JOIN      Product              as pr  on {cpi.product} = {pr.pk}" +
                "      JOIN      CustomerInquiryApprovalStatus as cias on {cias.pk} = {cpi.approvalStatus}" +
                "     }" +
                "WHERE {pr.code} = ?code and {cias.code} = 'APPROVED'";

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        SearchResult<Integer> result = flexibleSearchService.search(query);
        return result.getResult().getFirst();

    }

    private int getCountPublication(String productCode, boolean total) {

        String queryString = "SELECT COUNT( DISTINCT {cr.pk})" +
                "FROM {" +
                "                CustomerReview as cr" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "      JOIN      CustomerReviewApprovalType as cias on {cias.pk} = {cr.approvalStatus}" +
                "     } WHERE {pr.code} = ?code ";


        if(!total){
            queryString = queryString + " AND {cias.code} = 'approved'";
        }

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        SearchResult<Integer> result = flexibleSearchService.search(query);
        return result.getResult().getFirst();

    }


    private int getResponseTime(String productCode) {

        String queryString = "SELECT {cpi.creationTime}, {cpi.answerDate}" +
                "    FROM {" +
                "        CustomerProductInquiry as cpi" +
                "        JOIN      Product              as pr  on {cpi.product} = {pr.pk}" +
                "    }" +
                "    WHERE {pr.code} = ?code";

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Arrays.asList(Date.class, Date.class));
        query.addQueryParameter("code", productCode);
        final SearchResult<List<Object>> result = flexibleSearchService.search(query);
        Map<Date, Date> couponRedemptionsMap;

        if (result.getResult().isEmpty()){
            return 0;
        }


        couponRedemptionsMap = result.getResult().stream()
                .collect(Collectors.toMap(c -> (Date) c.getFirst(), c -> (Date) c.get(1)));

        List<Duration> durations = new ArrayList<>();

        for (Map.Entry<Date, Date> entry : couponRedemptionsMap.entrySet()) {
            durations.add( Duration.between(entry.getKey().toInstant(), entry.getValue().toInstant()));
        }

        // Calculate the average
        double averageInMinutes = calculateAverageDurationInMinutes(durations);

        return (int) averageInMinutes;
    }

    public static double calculateAverageDurationInMinutes(List<Duration> durations) {
        if (durations == null || durations.isEmpty()) {
            return 0.0;
        }

        long totalMinutes = 0;
        for (Duration duration : durations) {
            // Convert each duration to total minutes
            totalMinutes += duration.toMinutes(); //
        }

        // Calculate the average by dividing the total sum by the count of durations
        return (double) totalMinutes / durations.size();
    }






    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }


}
