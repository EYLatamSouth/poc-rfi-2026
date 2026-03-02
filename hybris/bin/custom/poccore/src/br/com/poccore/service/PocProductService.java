package br.com.poccore.service;

import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public interface PocProductService {

    PocProductEngagementSummaryInfoData getEngagementSummary(String productCode);

}
