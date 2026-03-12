package br.com.poccore.service;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocCustomerReviewService {
    CustomerReviewModel getProductReviewById(String productCode, String reviewId);

    CustomerReviewRatingModel createProductReviewRating(UserModel ratingUser, String productCode, String reviewId, boolean helpful)  throws IllegalArgumentException, IllegalStateException ;

    CustomerReviewRatingModel findCustomerReviewRating(CustomerReviewModel review, CustomerModel rater);
}
