package br.com.pocfacades.product.impl;

import br.com.poccore.service.PocProductService;
import br.com.pocfacades.product.PocProductFacade;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.search.restriction.SearchRestrictionService;

public class DefaultPocProductFacade implements PocProductFacade {

    private SearchRestrictionService searchRestrictionService;
    private PocProductService pocProductService;

    /**
     * Will call the PocProductService to get the mapped Data object of the engagement statistics of the Product.
     *
     * @param productCode   The code for the target product.
     */
    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode) {
        PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData;
        pocProductEngagementSummaryInfoData = getPocProductService().getEngagementSummary(productCode);

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
