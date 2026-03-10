package br.com.pocfacades.product;

import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;

public interface PocProductFacade {
    PocProductEngagementSummaryInfoData getEngagementSummary(String productCode);
}
