package br.com.pocfacades.product;

import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public interface PocProductFacade {

    PocProductEngagementSummaryInfoData getEngagementSummary(String productCode);

}
