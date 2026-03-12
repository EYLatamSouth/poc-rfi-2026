package br.com.poccore.service.impl;

import br.com.poccore.dao.PocCustomerReviewDao;
import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerReviewServiceTest {

    @InjectMocks
    private DefaultPocCustomerReviewService service;
    @Mock
    private ModelService modelService;
    @Mock
    private PocCustomerReviewDao pocReviewDao;
    @Mock
    private CustomerReviewRatingModel customerReviewRatingModel;
    @Mock
    private CustomerReviewModel customerReviewModel;
    @Mock
    private CustomerModel customerModel;
    @Mock
    private UserModel userModel;

    @Before
    public void setUp() {
        service.setModelService(modelService);
        service.setPocCustomerReviewDao(pocReviewDao);

        doNothing().when(modelService).save(any());
        doNothing().when(modelService).refresh(any());
    }

    @Test
    public void testFindProductReview_ById_Success() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        
        service.getProductReviewById("Product", "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindProductReview_ById_IllegalArgumentException() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(null);
        service.getProductReviewById("Product", "1");
    }

    @Test
    public void testCreateProductReviewRating_CreationSuccess() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(null);
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());
        when(modelService.create(CustomerReviewRatingModel.class)).thenReturn(new CustomerReviewRatingModel());

        CustomerReviewRatingModel actual = service.createProductReviewRating(customerModel, "product", "1", true);
        verify(modelService, times(1)).save(any());
        assertEquals(customerReviewModel, actual.getCustomerReview());
    }

    @Test
    public void testCreateProductReviewRating_UpdateSuccess() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(customerReviewRatingModel);
        when(customerReviewRatingModel.getCustomerReview()).thenReturn(customerReviewModel);
        when(customerReviewRatingModel.getIsUseful()).thenReturn(false);
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        CustomerReviewRatingModel actual = service.createProductReviewRating(customerModel, "product", "1", true);
        verify(modelService, times(1)).save(any());
        assertEquals(customerReviewModel, actual.getCustomerReview());
    }

    @Test
    public void testCreateProductReviewRating_NoOperationSuccess() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(customerReviewRatingModel);
        when(customerReviewRatingModel.getIsUseful()).thenReturn(true);
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        service.createProductReviewRating(customerModel, "product", "1", true);
        verify(modelService, never()).save(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProductReviewRating_IllegalArgumentException() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        when(customerReviewModel.getUser()).thenReturn(null);

        service.createProductReviewRating(customerModel, "product", "1", true);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateProductReviewRating_IllegalStateException() {
        when(pocReviewDao.findProductReviewById(anyString(), anyString())).thenReturn(customerReviewModel);
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        service.createProductReviewRating(userModel,"product", "1", true);
    }

}