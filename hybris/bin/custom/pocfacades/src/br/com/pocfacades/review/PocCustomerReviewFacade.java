package br.com.pocfacades.review;

public interface PocCustomerReviewFacade {
    void createProductReviewRating(String productCode, String reviewId, boolean helpful);
}
