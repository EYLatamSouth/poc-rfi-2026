package br.com.poccore.dao.impl;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocReviewDaoTest {

    @InjectMocks
    private DefaultPocCustomerReviewDao dao;

    @Mock
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

    @Mock
    private FlexibleSearchService flexibleSearchService;

    @Before
    public void setUp() {
        dao.setPaginatedFlexibleSearchService(paginatedFlexibleSearchService);
        dao.setFlexibleSearchService(flexibleSearchService);
    }

    @Test
    public void testFindNthProductReview() {
        SearchPageData expected = mock(SearchPageData.class);
        doReturn(expected).when(paginatedFlexibleSearchService).search(any());
        SearchPageData<CustomerReviewModel> actual = dao.findNthProductReview("productCode", 1);
        assertNotNull(actual);
        verify(paginatedFlexibleSearchService, times(1)).search(any());
    }

    @Test
    public void testFindReviewRatingByReviewAndRater() {
        doReturn(mock(SearchPageData.class)).when(flexibleSearchService).search((FlexibleSearchQuery) any());
        CustomerReviewRatingModel actual = dao.findReviewRatingByReviewAndRater(PK.fromLong(2L), PK.fromLong(1L));
        assertNotNull(actual);
        verify(flexibleSearchService, times(1)).search(eq(new FlexibleSearchQuery("")));
    }
}