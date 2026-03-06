package br.com.pocfacades.review.impl;

import br.com.poccore.service.PocCustomerReviewService;
import br.com.pocfacades.review.PocCustomerReviewFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultPocCutomerReviewFacade implements PocCustomerReviewFacade {
    private final Logger LOG = LoggerFactory.getLogger(DefaultPocCutomerReviewFacade.class);

    private PocCustomerReviewService pocReviewService;



    /**
     * Will call the PocReviewService to create a ProductReviewRating.
     *
     * @param productCode   The code for the target product.
     * @param nth           The chronological position.
     * @param helpful       Review rate value.
     *
     * @throws IllegalArgumentException when productCode or nth doesn't follow requirements
     */
    @Override
    public void createProductReviewRating(String productCode, int nth, boolean helpful) throws IllegalArgumentException {
        try {
            getPocReviewService().createProductReviewRating(productCode, nth, helpful);
        }
        catch (Exception e) {
            LOG.error("Error occurred while creating CustomerReviewRating: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PocCustomerReviewService getPocReviewService() {
        return pocReviewService;
    }

    public void setPocReviewService(PocCustomerReviewService pocReviewService) {
        this.pocReviewService = pocReviewService;
    }
}
