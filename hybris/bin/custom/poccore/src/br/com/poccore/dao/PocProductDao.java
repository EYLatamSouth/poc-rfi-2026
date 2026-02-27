package br.com.poccore.dao;

import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public interface PocProductDao {

    PocProductEngagementSummaryInfoData getEngagementSummary(String productCode);

}
