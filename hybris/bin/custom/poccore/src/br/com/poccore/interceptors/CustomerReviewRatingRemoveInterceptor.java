package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;


public class CustomerReviewRatingRemoveInterceptor implements RemoveInterceptor<CustomerReviewRatingModel> {

    @Override
    public void onRemove(CustomerReviewRatingModel rating, InterceptorContext ctx) throws InterceptorException {
        CustomerReviewModel review = rating.getCustomerReview();
        if (review != null && review.getHelpfulnessRating() != null && rating.getIsUseful() != null) {
            Integer helpfulnessRating = review.getHelpfulnessRating();
            Integer reversedUsefulValue = (rating.getIsUseful() ? -1 : 1);
            review.setHelpfulnessRating(helpfulnessRating + reversedUsefulValue);
            ctx.getModelService().save(review);
        }
    }
}
