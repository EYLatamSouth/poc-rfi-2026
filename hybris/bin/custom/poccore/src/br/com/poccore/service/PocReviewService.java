package br.com.poccore.service;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocReviewService {
    CustomerReviewModel findNthProductReview(String productCode, int nth);

    CustomerReviewRatingModel createProductReviewRating(String productCode, int nth, boolean helpful)  throws IllegalArgumentException, IllegalStateException ;

    CustomerReviewRatingModel findCustomerReviewRating(CustomerReviewModel review, CustomerModel rater);
}
