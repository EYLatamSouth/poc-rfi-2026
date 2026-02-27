package br.com.poccore.dao.impl;

import br.com.poccore.dao.PocProductDao;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.List;

public class DefaultPocProductDao implements PocProductDao {

    private FlexibleSearchService flexibleSearchService;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){
        return null;
    }


    public SearchResult<List<Object>> getEngagementSummary(List<Object> orderPks) {

        //média de rating
        String queryStringRatingAVG = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }" +
                "WHERE {pr.code} = ?code and {crr.isUseful} = 1";

        FlexibleSearchQuery queryRatingAVG = new FlexibleSearchQuery(queryStringRatingAVG);
        queryRatingAVG.setResultClassList(Collections.singletonList(Integer.class));
        queryRatingAVG.addQueryParameter("code", orderPks);

        SearchResult<Integer> resultRatingAVG = flexibleSearchService.search(queryRatingAVG);
        int count = resultRatingAVG.getResult().getFirst();


        String queryStringRatingAVGTotal = "SELECT COUNT (*)" +
                "FROM {" +
                "                CustomerReviewRating as crr" +
                "      JOIN      CustomerReview       as cr  on {cr.pk} = {crr.customerReview}" +
                "      JOIN      Product              as pr  on {cr.product} = {pr.pk}" +
                "     }" +
                "WHERE {pr.code} = ?code  and {crr.isUseful} IS NOT NULL";

        FlexibleSearchQuery queryRatingAVGTotal = new FlexibleSearchQuery(queryStringRatingAVGTotal);
        queryRatingAVGTotal.setResultClassList(Collections.singletonList(Integer.class));
        queryRatingAVGTotal.addQueryParameter("code", orderPks);

        SearchResult<Integer> resulRatingAVGTotal = flexibleSearchService.search(queryRatingAVG);
        int countTotal = resulRatingAVGTotal.getResult().getFirst();

       //% Verified

        return  null;
    }


    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }


}
