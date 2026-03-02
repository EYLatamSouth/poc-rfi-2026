package br.com.poccore.dao.impl;

import br.com.poc.occ.dto.product.PocProductReviewsInfoData;
import br.com.poccore.dao.PocProductDao;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

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
        pocProductEngagementSummaryInfoData.setRating(rating);
        //% Verified
        double verified = 0.0;
        int verifiedAVG  = getCountVerified(productCode, true);
        int verifiedAVGTotal  = getCountVerified(productCode, false);
        if(verifiedAVGTotal > 0) {
            verified = (((double) verifiedAVG / verifiedAVGTotal) * 100);
        }
        pocProductEngagementSummaryInfoData.setVerified(verified);

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
        int publicationAVG  = getCountPublication(productCode, false);
        int publicationAVGTotal  = getCountPublication(productCode, true);
        if(publicationAVGTotal > 0) {
            publication = (((double) publicationAVG / publicationAVGTotal) * 100);
        }
        pocProductEngagementSummaryInfoData.setPublication(publication);

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

        String queryString = "SELECT COUNT({cr.pk})" +
                "FROM {" +
                "                CustomerReview as cr" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "      JOIN      CustomerReviewApprovalType as cias on {cias.pk} = {cr.approvalStatus}" +
                "     }";

        StringBuilder query = new StringBuilder(queryString);

        if(total){
            query.append(" WHERE {pr.code} = ?code and {cias.code} = 'APPROVED'");
        } else {
            query.append(" WHERE {pr.code} = ?code ");
        }

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("code", productCode);
        fQuery.setResultClassList(Collections.singletonList(Integer.class));

        SearchResult<Integer> result = flexibleSearchService.search(fQuery);
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
            Duration duration = Duration.between((Temporal) entry.getKey(), (Temporal) entry.getValue());
            durations.add(duration);
        }

        // Calculate the average
        Duration averageDuration = calculateAverageDuration(durations);

        return (int) averageDuration.toMinutes();
    }

    /**
     * Calculates the average Duration from a list of Durations.
     * @param durations The list of durations.
     * @return The average duration.
     */
    public static Duration calculateAverageDuration(List<Duration> durations) {
        if (durations == null || durations.isEmpty()) {
            return Duration.ZERO;
        }

        // Use streams to calculate the average of nanoseconds
        LongSummaryStatistics stats = durations.stream()
                .mapToLong(Duration::toNanos) // Convert each Duration to nanoseconds
                .summaryStatistics(); // Get summary statistics

        // The average is returned as a double. Convert it back to a long for Duration.ofNanos()
        long meanNanos = (long) stats.getAverage();

        // Create a new Duration from the calculated mean nanoseconds
        return Duration.ofNanos(meanNanos);
    }






    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }


}
