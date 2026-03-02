package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Objects;

public class CustomerReviewRatingInterceptor implements ValidateInterceptor<CustomerReviewRatingModel> {

    /**
     * Adds or subtract to helpfulness rating depending on change.
     *
     * @param rating The Customer Review Rating that's being saved.
     * @param ctx    Interceptor Context.
     */
    @Override
    public void onValidate(CustomerReviewRatingModel rating, InterceptorContext ctx) {
        if (!ctx.isNew(rating) && ctx.isModified(rating, CustomerReviewRatingModel.ISUSEFUL)) {
            CustomerReviewModel review = rating.getCustomerReview();
            Integer helpfulnessRating = review.getHelpfulnessRating();
            Integer isUsefulValue = (rating.getIsUseful() ? 1 : -1);
            if (Objects.nonNull(helpfulnessRating)) {
                review.setHelpfulnessRating(review.getHelpfulnessRating() + isUsefulValue);
            } else {
                review.setHelpfulnessRating(isUsefulValue);
            }
        }
    }
}
