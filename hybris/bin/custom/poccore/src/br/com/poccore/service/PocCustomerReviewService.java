package br.com.poccore.service;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocCustomerReviewService {
    CustomerReviewModel findNthProductReview(String productCode, int nth);

    CustomerReviewRatingModel createProductReviewRating(UserModel ratingUser, String productCode, int nth, boolean helpful)  throws IllegalArgumentException, IllegalStateException ;

    CustomerReviewRatingModel findCustomerReviewRating(CustomerReviewModel review, CustomerModel rater);
}
