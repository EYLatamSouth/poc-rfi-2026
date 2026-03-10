package br.com.poccore.event;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerReviewEventListenerTest {

    @InjectMocks
    private CustomerReviewEventListener listener;

    @Mock
    private CustomerReviewEvent customerReviewEvent;

    @Mock
    private CustomerReviewModel customerReviewModel;

    @Mock
    private CustomerModel customerModel;

    @Mock
    private OrderModel orderModel;

    @Mock
    private OrderEntryModel orderEntryModel;

    @Mock
    private ProductModel productModel;

    @Before
    public void setUp() {
        when(customerReviewEvent.getCustomerReview()).thenReturn(customerReviewModel);
        when(customerReviewModel.getUser()).thenReturn(customerModel);
    }

    @Test
    public void testOnEventWhenCustomerHasBoughtProduct() {
        when(customerReviewModel.getProduct()).thenReturn(productModel);
        when(orderEntryModel.getProduct()).thenReturn(productModel);
        when(orderModel.getEntries()).thenReturn(List.of(orderEntryModel));

        final Collection<OrderModel> orders = new ArrayList<>();
        orders.add(orderModel);

        when(customerModel.getOrders()).thenReturn(orders);

        listener.onEvent(customerReviewEvent);
    }

    @Test
    public void testOnEventWhenCustomerHasNotBoughtProduct() {
        ProductModel productBought = mock(ProductModel.class);
        ProductModel productReview = mock(ProductModel.class);

        productBought.setCode("2278102");
        productReview.setCode("2231913");

        when(customerReviewModel.getProduct()).thenReturn(productReview);
        when(orderEntryModel.getProduct()).thenReturn(productBought);
        when(orderModel.getEntries()).thenReturn(List.of(orderEntryModel));

        final Collection<OrderModel> orders = new ArrayList<>();
        orders.add(orderModel);

        when(customerModel.getOrders()).thenReturn(orders);

        listener.onEvent(customerReviewEvent);
    }

}
