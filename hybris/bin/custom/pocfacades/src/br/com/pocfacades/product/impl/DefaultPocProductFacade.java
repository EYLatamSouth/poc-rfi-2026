package br.com.pocfacades.product.impl;

import br.com.pocfacades.product.PocProductFacade;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public class DefaultPocProductFacade implements PocProductFacade {

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){

        final PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData = new PocProductEngagementSummaryInfoData();


        pocProductEngagementSummaryInfoData.setDistribution(10.0);

        return pocProductEngagementSummaryInfoData;


    }


}
