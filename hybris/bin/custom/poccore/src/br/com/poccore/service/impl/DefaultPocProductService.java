package br.com.poccore.service.impl;

import br.com.poccore.dao.PocProductDao;
import br.com.poccore.service.PocProductService;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;

public class DefaultPocProductService implements PocProductService {

    private PocProductDao pocProductDao;


    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode){

        return  getPocProductDao().getEngagementSummary(productCode);

    }


    public PocProductDao getPocProductDao() {
        return pocProductDao;
    }

    public void setPocProductDao(PocProductDao pocProductDao) {
        this.pocProductDao = pocProductDao;
    }



}
