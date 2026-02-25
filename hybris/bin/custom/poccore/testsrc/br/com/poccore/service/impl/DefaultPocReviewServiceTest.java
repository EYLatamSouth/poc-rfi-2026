package br.com.poccore.service.impl;

import br.com.poccore.dao.PocReviewDao;
import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocReviewServiceTest {

    @InjectMocks
    private DefaultPocReviewService service;
    @Mock
    private UserService userService;
    @Mock
    private ModelService modelService;
    @Mock
    private PocReviewDao pocReviewDao;
    @Mock
    private SearchPageData<CustomerReviewModel> searchPageData;
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
        service.setUserService(userService);
        service.setModelService(modelService);
        service.setPocReviewDao(pocReviewDao);

        doNothing().when(modelService).save(any());
        doNothing().when(modelService).refresh(any());
    }

    @Test
    public void testFindNthProductReview_Sucess() {
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        service.findNthProductReview("Product", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNthProductReview_IllegalArgumentException() {
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(null);
        service.findNthProductReview("Product", 1);
    }

    @Test(expected = UnknownIdentifierException.class)
    public void testFindNthProductReview_UnknownIdentifierException() {
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(List.of());
        service.findNthProductReview("Product", 1);
    }

    @Test(expected = AmbiguousIdentifierException.class)
    public void testFindNthProductReview_AmbiguousIdentifierException() {
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel, customerReviewModel));
        service.findNthProductReview("Product", 1);
    }

    @Test
    public void testCreateProductReviewRating_CreationSuccess() {
        when(userService.getCurrentUser()).thenReturn(customerModel);
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(null);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());
        when(modelService.create(CustomerReviewRatingModel.class)).thenReturn(new CustomerReviewRatingModel());

        CustomerReviewRatingModel actual = service.createProductReviewRating("product", 1, true);
        verify(modelService, times(1)).save(any());
        assertEquals(customerReviewModel, actual.getCustomerReview());
    }

    @Test
    public void testCreateProductReviewRating_UpdateSuccess() {
        when(userService.getCurrentUser()).thenReturn(customerModel);
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(customerReviewRatingModel);
        when(customerReviewRatingModel.getCustomerReview()).thenReturn(customerReviewModel);
        when(customerReviewRatingModel.getIsUseful()).thenReturn(false);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        CustomerReviewRatingModel actual = service.createProductReviewRating("product", 1, true);
        verify(modelService, times(1)).save(any());
        assertEquals(customerReviewModel, actual.getCustomerReview());
    }

    @Test
    public void testCreateProductReviewRating_NoOperationSuccess() {
        when(userService.getCurrentUser()).thenReturn(customerModel);
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(pocReviewDao.findReviewRatingByReviewAndRater(any(), any())).thenReturn(customerReviewRatingModel);
        when(customerReviewRatingModel.getIsUseful()).thenReturn(true);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        service.createProductReviewRating("product", 1, true);
        verify(modelService, never()).save(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProductReviewRating_IllegalArgumentException() {
        when(userService.getCurrentUser()).thenReturn(customerModel);
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        when(customerReviewModel.getUser()).thenReturn(null);

        service.createProductReviewRating("product", 1, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateProductReviewRating_IllegalStateException() {
        when(userService.getCurrentUser()).thenReturn(userModel);
        when(pocReviewDao.findNthProductReview(anyString(), anyInt())).thenReturn(searchPageData);
        when(searchPageData.getResults()).thenReturn(List.of(customerReviewModel));
        when(customerReviewModel.getUser()).thenReturn(new CustomerModel());

        service.createProductReviewRating("product", 1, true);
    }

}