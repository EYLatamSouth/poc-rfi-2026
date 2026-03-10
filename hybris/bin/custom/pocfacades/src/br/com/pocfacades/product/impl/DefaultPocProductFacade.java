package br.com.pocfacades.product.impl;

import br.com.poccore.service.PocProductService;
import br.com.pocfacades.product.PocProductFacade;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.search.restriction.SearchRestrictionService;

public class DefaultPocProductFacade implements PocProductFacade {

    private SearchRestrictionService searchRestrictionService;
    private PocProductService pocProductService;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){
        searchRestrictionService.disableSearchRestrictions();
        PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData;
        pocProductEngagementSummaryInfoData = getPocProductService().getEngagementSummary(productCode);
        searchRestrictionService.enableSearchRestrictions();

        return pocProductEngagementSummaryInfoData;

    }

    public PocProductService getPocProductService() {
        return pocProductService;
    }

    public void setPocProductService(PocProductService pocProductService) {
        this.pocProductService = pocProductService;
    }

    public SearchRestrictionService getSearchRestrictionService() {
        return searchRestrictionService;
    }

    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService) {
        this.searchRestrictionService = searchRestrictionService;
    }


}
