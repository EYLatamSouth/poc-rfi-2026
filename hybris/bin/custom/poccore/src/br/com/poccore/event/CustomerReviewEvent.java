package br.com.poccore.event;

import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class CustomerReviewEvent extends AbstractEvent {
    private CustomerReviewModel customerReview;

    public CustomerReviewEvent(CustomerReviewModel customerReview){
        this.customerReview = customerReview;
    }

    public CustomerReviewModel getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(CustomerReviewModel customerReview) {
        this.customerReview = customerReview;
    }
}
