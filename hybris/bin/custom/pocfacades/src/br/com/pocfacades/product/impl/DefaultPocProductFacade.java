package br.com.pocfacades.product.impl;

import br.com.poccore.service.PocProductService;
import br.com.pocfacades.product.PocProductFacade;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public class DefaultPocProductFacade implements PocProductFacade {

    private PocProductService pocProductService;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){

       return  getPocProductService().getEngagementSummary(productCode);

    }

    public PocProductService getPocProductService() {
        return pocProductService;
    }

    public void setPocProductService(PocProductService pocProductService) {
        this.pocProductService = pocProductService;
    }

}
