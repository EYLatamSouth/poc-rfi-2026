package br.com.pocfacades.review.impl;

import br.com.poccore.service.PocReviewService;
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
public class DefaultPocReviewFacadeTest {
    @InjectMocks
    private DefaultPocReviewFacade facade;
    @Mock
    private PocReviewService pocReviewService;

    @Before
    public void setUp() {
        facade.setPocReviewService(pocReviewService);
        when(pocReviewService.createProductReviewRating(anyString(), anyInt(), anyBoolean())).thenReturn(null);
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

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProductReviewRating_BlancString() {
        facade.createProductReviewRating("", 10, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProductReviewRating_NullString() {
        facade.createProductReviewRating(null, 10, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProductReviewRating_NegativeNth() {
        facade.createProductReviewRating("product", -10, true);
    }
}