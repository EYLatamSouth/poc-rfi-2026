package br.com.poccore.interceptors;

import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

public class CustomerReviewPrepareInterceptor implements PrepareInterceptor<CustomerReviewModel> {

    @Override
    public void onPrepare(CustomerReviewModel customerReviewModel, InterceptorContext ctx) throws InterceptorException {
        if(ctx.isModified(customerReviewModel, CustomerReviewModel.RATING)) {
            if(customerReviewModel.getRating() == null || customerReviewModel.getRating() < 1) {
                customerReviewModel.setRating(1.0);
            } else if(customerReviewModel.getRating() > 5) {
                customerReviewModel.setRating(5.0);
            }
        }
    }

}
