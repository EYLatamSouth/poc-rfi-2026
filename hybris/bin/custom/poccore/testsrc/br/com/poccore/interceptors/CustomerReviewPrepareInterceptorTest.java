package br.com.poccore.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerReviewPrepareInterceptorTest {

    @InjectMocks
    private CustomerReviewPrepareInterceptor customerReviewPrepareInterceptor;

    @Before
    public void setUp() {}

    @Mock
    private InterceptorContext ctx;

    @Test
    public void testRatingNotModified() throws InterceptorException {
        CustomerReviewModel model = new CustomerReviewModel();
        when(ctx.isModified(model, CustomerReviewModel.RATING)).thenReturn(false);

        customerReviewPrepareInterceptor.onPrepare(model, ctx);

        assertNull(model.getRating());
    }

    @Test
    public void testRatingNullOrLessThenOne() throws InterceptorException {
        final Double one = 1.0;

        CustomerReviewModel model = new CustomerReviewModel();
        when(ctx.isModified(model, CustomerReviewModel.RATING)).thenReturn(true);

        customerReviewPrepareInterceptor.onPrepare(model, ctx);

        assertNotNull(model.getRating());
        assertEquals(one, model.getRating());

        model.setRating(-10.0);

        customerReviewPrepareInterceptor.onPrepare(model, ctx);

        assertNotNull(model.getRating());
        assertEquals(one, model.getRating());
    }

    @Test
    public void testRatingGreaterThenFive() throws InterceptorException {
        final Double five = 5.0;

        CustomerReviewModel model = new CustomerReviewModel();
        model.setRating(7.5);

        when(ctx.isModified(model, CustomerReviewModel.RATING)).thenReturn(true);

        customerReviewPrepareInterceptor.onPrepare(model, ctx);

        assertNotNull(model.getRating());
        assertEquals(five, model.getRating());

        Double two = 2.5;
        model.setRating(two);

        customerReviewPrepareInterceptor.onPrepare(model, ctx);

        assertEquals(two, model.getRating());
    }
}
