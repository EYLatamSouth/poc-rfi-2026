package br.com.pocfacades.review;

public interface PocReviewFacade {
    void createProductReviewRating(String productCode, int reviewId, boolean helpful);
}
