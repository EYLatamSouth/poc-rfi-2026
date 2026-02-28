package br.com.poccore.dao.impl;

import br.com.poccore.dao.PocProductDao;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultPocProductDao implements PocProductDao {

    private FlexibleSearchService flexibleSearchService;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){

         PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData = new PocProductEngagementSummaryInfoData();

         //média de rating
        int ratingAVG  = getCountRatingAVG(productCode, true);
        int ratingAVGTotal  = getCountRatingAVG(productCode, false);
        Double rating = (double) (( ratingAVG / ratingAVGTotal ) * 100);
        pocProductEngagementSummaryInfoData.setRating(rating);

        //% Verified
        int verifiedAVG  = getCountVerified(productCode, true);
        int verifiedAVGTotal  = getCountVerified(productCode, false);
        Double verified = (double) (( verifiedAVG / verifiedAVGTotal ) * 100);
        pocProductEngagementSummaryInfoData.setVerified(verified);

        //% top N reviews úteis
        SortedMap<String, Integer> listNReviews = getTopNRating(productCode);

        /*

        Contagem de perguntas - somar o total de inquiry com approved para o produto
SELECT COUNT({cpi.pk})
FROM {
                CustomerProductInquiry as cpi
      JOIN      Product              as pr  on {cpi.product} = {pr.pk}
      JOIN      CustomerInquiryApprovalStatus as cias on {cias.pk} = {cpi.approvalStatus}
     }
WHERE {pr.code} = '2278102' and {cias.code} = 'APPROVED'

Taxa de publicação - CustomerReview - pegar tabela por produto e somar todas as publicadas (approved ) por total de reviews

SELECT COUNT({cr.pk})
FROM {
                CustomerReview as cr
      JOIN      Product              as pr  on {cr.product} = {pr.pk}
      JOIN      CustomerReviewApprovalType as cias on {cias.pk} = {cr.approvalStatus}
     }
WHERE {pr.code} = '2278102' and {cias.code} = 'APPROVED'


SELECT COUNT({cr.pk})
FROM {
                CustomerReview as cr
      JOIN      Product              as pr  on {cr.product} = {pr.pk}
     }
WHERE {pr.code} = '2278102'

Tempo médio do inquiry - passar por todos os enquiry do produto e

SELECT {cpi.creationTime}, {cpi.answerDate}
FROM {
                CustomerProductInquiry as cpi
      JOIN      Product              as pr  on {cpi.product} = {pr.pk}
     }
WHERE {pr.code} = '2278102'

         */



        return  pocProductEngagementSummaryInfoData;
    }


    private int getCountRatingAVG(String productCode, boolean isUseful) {

        String queryString = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }" +
                "WHERE {pr.code} = ?code and {crr.isUseful} = ?isUseful";

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);

        if(isUseful){
            query.addQueryParameter("isUseful", 1);
        } else{
            query.addQueryParameter("isUseful", "IS NOT NULL");
        }

        SearchResult<Integer> result = flexibleSearchService.search(query);
        return result.getResult().getFirst();

    }

    private int getCountVerified(String productCode, boolean hasBoughtProduct) {

        String queryString = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }" +
                "WHERE {pr.code} = ?code  and {crr.isUseful} IS NOT NULL and  {cr.hasBoughtProduct} = ?hasBoughtProduct";

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);

        if(hasBoughtProduct){
            query.addQueryParameter("hasBoughtProduct", 1);
        } else{
            query.addQueryParameter("hasBoughtProduct", "IS NOT NULL");
        }

        SearchResult<Integer> result = flexibleSearchService.search(query);
        return result.getResult().getFirst();

    }

    private SortedMap<String, Integer> getTopNRating(String productCode) {

        String queryString = "SELECT {cr.headline}, count({crr.isUseful})" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }" +
                "WHERE {pr.code} = ?code and {crr.isUseful} = 1 and  {cr.hasBoughtProduct} = 1" +
                "group by {cr.headline}";

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
            //System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            if(entry.getValue().equals(0)){
                couponRedemptionsMapSorted.remove(entry.getKey(),entry.getValue());
            }
        }

        return couponRedemptionsMapSorted;

    }



    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }


}
