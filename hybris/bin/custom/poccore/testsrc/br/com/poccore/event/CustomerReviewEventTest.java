package br.com.poccore.event;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerReviewEventTest {
    @Mock
    private CustomerReviewEvent customerReviewEvent;

    @Mock
    private CustomerReviewModel customerReviewModel;

    @Before
    public void setUp() {
        customerReviewModel = new CustomerReviewModel();
        customerReviewEvent = new CustomerReviewEvent(customerReviewModel);
    }

    @Test
    public void testOrderStatusEventCreation() {
        assertNotNull(customerReviewEvent);
        assertEquals(customerReviewModel, customerReviewEvent.getCustomerReview());
    }

    @Test
    public void testSetOrder() {
        customerReviewEvent.setCustomerReview(customerReviewModel);
        assertEquals(customerReviewModel, customerReviewEvent.getCustomerReview());
    }

}
