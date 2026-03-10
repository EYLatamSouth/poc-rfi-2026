package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerReviewRatingRemoveInterceptorTest {
    @InjectMocks
    private CustomerReviewRatingRemoveInterceptor interceptor;
    @Mock
    private CustomerReviewRatingModel rating;
    @Mock
    private CustomerReviewModel review;
    @Mock
    private ModelService modelService;
    @Mock
    private InterceptorContext ctx;

    @Before
    public void setUp() {
        when(ctx.getModelService()).thenReturn(modelService);
    }

    @Test
    public void testOnRemove_SuccessPositive() throws InterceptorException {
        when(rating.getCustomerReview()).thenReturn(review);
        when(review.getHelpfulnessRating()).thenReturn(10);
        when(rating.getIsUseful()).thenReturn(true);
        interceptor.onRemove(rating, ctx);
        verify(modelService, times(1)).save(any());
        verify(review, times(1)).setHelpfulnessRating(9);
    }

    @Test
    public void testOnRemove_SuccessNegative() throws InterceptorException {
        when(rating.getCustomerReview()).thenReturn(review);
        when(review.getHelpfulnessRating()).thenReturn(10);
        when(rating.getIsUseful()).thenReturn(false);
        interceptor.onRemove(rating, ctx);
        verify(modelService, times(1)).save(any());
        verify(review, times(1)).setHelpfulnessRating(11);
    }

    @Test
    public void testOnRemove_NullReview() throws InterceptorException  {
        when(rating.getCustomerReview()).thenReturn(null);
        interceptor.onRemove(rating, ctx);
        verify(modelService, never()).save(any());
    }

    @Test
    public void testOnRemove_NullHelpfulnessRating() throws InterceptorException  {
        when(rating.getCustomerReview()).thenReturn(review);
        when(review.getHelpfulnessRating()).thenReturn(null);
        interceptor.onRemove(rating, ctx);
        verify(modelService, never()).save(any());
    }

    @Test
    public void testOnRemove_NullIsUseful() throws InterceptorException  {
        when(rating.getCustomerReview()).thenReturn(review);
        when(review.getHelpfulnessRating()).thenReturn(10);
        when(rating.getIsUseful()).thenReturn(null);
        interceptor.onRemove(rating, ctx);
        verify(modelService, never()).save(any());
    }
}