package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CustomerReviewRatingValidateInterceptorTest {

    @InjectMocks
    private CustomerReviewRatingValidateInterceptor interceptor;
    @Mock
    private CustomerReviewRatingModel ratingModel;
    @Mock
    private CustomerReviewModel reviewModel;
    @Mock
    private ModelService modelService;
    @Mock
    private InterceptorContext ctx;

    @Before
    public void setUp() {
        when(reviewModel.getHelpfulnessRating()).thenReturn(0);
        when(ratingModel.getCustomerReview()).thenReturn(reviewModel);
        when(ctx.isNew(any())).thenReturn(false);
        when(ctx.isModified(any(), any())).thenReturn(true);
        when(ctx.getModelService()).thenReturn(modelService);
    }

    @Test
    public void testOnValidate_NewSuccess() {
        when(ctx.isNew(any())).thenReturn(true);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(any());
    }

    @Test
    public void testOnValidate_NotModifiedSuccess() {
        when(ctx.isModified(any(), any())).thenReturn(false);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, never()).setHelpfulnessRating(any());
    }

    @Test
    public void testOnValidate_IncrementSuccess() {
        when(ratingModel.getIsUseful()).thenReturn(true);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(1);
    }

    @Test
    public void testOnValidate_IncrementAtZeroSuccess() {
        when(ratingModel.getIsUseful()).thenReturn(true);
        when(reviewModel.getHelpfulnessRating()).thenReturn(-1);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(1);
    }

    @Test
    public void testOnValidate_DecrementSuccess() {
        when(ratingModel.getIsUseful()).thenReturn(false);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(-1);
    }

    @Test
    public void testOnValidate_IncrementOnNullSuccess() {
        when(reviewModel.getHelpfulnessRating()).thenReturn(null);
        when(ratingModel.getIsUseful()).thenReturn(true);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(1);
    }

    @Test
    public void testOnValidate_DecrementOnNullSuccess() {
        when(reviewModel.getHelpfulnessRating()).thenReturn(null);
        when(ratingModel.getIsUseful()).thenReturn(false);
        interceptor.onValidate(ratingModel, ctx);
        verify(reviewModel, times(1)).setHelpfulnessRating(-1);
    }
}