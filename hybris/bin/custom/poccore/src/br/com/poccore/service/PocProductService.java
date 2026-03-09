package br.com.poccore.service;

import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;

public interface PocProductService {

    PocProductEngagementSummaryInfoData getEngagementSummary(String productCode);

}
