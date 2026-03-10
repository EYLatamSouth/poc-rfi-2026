package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Objects;

public class CustomerReviewRatingValidateInterceptor implements ValidateInterceptor<CustomerReviewRatingModel> {

    /**
     * Adds or subtract to helpfulness rating depending on change.
     *
     * @param rating The Customer Review Rating that's being saved.
     * @param ctx    Interceptor Context.
     */
    @Override
    public void onValidate(CustomerReviewRatingModel rating, InterceptorContext ctx) {
        if (ctx.isNew(rating) || ctx.isModified(rating, CustomerReviewRatingModel.ISUSEFUL)) {
            CustomerReviewModel review = rating.getCustomerReview();
            Integer helpfulnessRating = review.getHelpfulnessRating();
            Integer isUsefulValue = (BooleanUtils.isTrue(rating.getIsUseful()) ? 1 : -1);
            if (Objects.nonNull(helpfulnessRating)
                    && (helpfulnessRating + isUsefulValue != 0)) { // second condition allows for rating to go from -1 to 1 interchangeably
                review.setHelpfulnessRating(helpfulnessRating + isUsefulValue);
            } else {
                review.setHelpfulnessRating(isUsefulValue);
            }
            ctx.getModelService().save(review);
        }
    }
}