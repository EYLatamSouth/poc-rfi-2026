package br.com.pocfacades.review.impl;

import br.com.poccore.service.PocCustomerReviewService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerReviewFacadeTest {
    @InjectMocks
    private DefaultPocCustomerReviewFacade facade;
    @Mock
    private PocCustomerReviewService pocReviewService;
    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        facade.setPocReviewService(pocReviewService);
        facade.setUserService(userService);

        when(userService.getCurrentUser()).thenReturn(null);
        when(pocReviewService.createProductReviewRating(any(), anyString(), anyInt(), anyBoolean())).thenReturn(null);
    }

    @Test
    public void testCreateProductReviewRating_Success() {
        try {
            facade.createProductReviewRating("product", 10, true);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateProductReviewRating_Fail() {
        facade.setUserService(null);
        facade.createProductReviewRating("product", 10, true);
        fail();
    }
}