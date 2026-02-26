package br.com.pocfacades.review;

import de.hybris.platform.commercefacades.product.data.ReviewData;

import java.util.List;

public interface PocCustomerReviewFacade {
    void createProductReviewRating(String productCode, int reviewId, boolean helpful);
}
