package br.com.pocfacades.review;

import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocReviewFacade {
    void createProductReviewRating(String productCode, int reviewId, boolean helpful);
}
