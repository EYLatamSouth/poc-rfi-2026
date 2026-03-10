package br.com.poccore.interceptors;

import br.com.poccore.event.CustomerReviewEvent;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import jakarta.annotation.Resource;

public class CustomerReviewPrepareInterceptor implements PrepareInterceptor<CustomerReviewModel> {

    @Resource
    private EventService eventService;

    @Override
    public void onPrepare(CustomerReviewModel customerReviewModel, InterceptorContext ctx) throws InterceptorException {
        if (ctx.isModified(customerReviewModel, CustomerReviewModel.RATING)) {
            if (customerReviewModel.getRating() == null || customerReviewModel.getRating() < 1) {
                customerReviewModel.setRating(1.0);
            } else if (customerReviewModel.getRating() > 5) {
                customerReviewModel.setRating(5.0);
            }
        }

        getEventService().publishEvent(new CustomerReviewEvent(customerReviewModel));
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
