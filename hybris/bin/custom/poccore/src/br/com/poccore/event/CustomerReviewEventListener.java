package br.com.poccore.event;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public class CustomerReviewEventListener extends AbstractEventListener<CustomerReviewEvent> {

    @Override
    protected void onEvent(CustomerReviewEvent event) {
        CustomerReviewModel customerReview = event.getCustomerReview();
        CustomerModel customer = (CustomerModel) customerReview.getUser();
        ProductModel product = customerReview.getProduct();

        boolean customerHasBoughtProduct = customer.getOrders().stream()
                .anyMatch(order -> order.getEntries().stream()
                        .anyMatch(entry -> entry.getProduct().equals(product)));

        if (customerHasBoughtProduct) {
            customerReview.setHasBoughtProduct(Boolean.TRUE);
        }
    }

}
