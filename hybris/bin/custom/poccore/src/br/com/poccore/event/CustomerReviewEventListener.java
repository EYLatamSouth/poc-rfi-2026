package br.com.poccore.event;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class CustomerReviewEventListener extends AbstractEventListener<CustomerReviewEvent> {
    private ModelService modelService;

    @Override
    protected void onEvent(CustomerReviewEvent event) {
        CustomerReviewModel customerReview = event.getCustomerReview();
        CustomerModel customer = (CustomerModel) customerReview.getUser();
        ProductModel product = customerReview.getProduct();
        Boolean customerHasBoughtProduct = Boolean.FALSE;

        for(AbstractOrderModel orders : customer.getOrders()){
            if(Boolean.TRUE.equals(orders.getEntries().stream().anyMatch(e -> e.getProduct().equals(product)))){
                customerHasBoughtProduct = Boolean.TRUE;
            }
        }

        if(Boolean.TRUE.equals(customerHasBoughtProduct)){
            customerReview.setHasBoughtProduct(Boolean.TRUE);
            //getModelService().save(customerReview);
        }
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
