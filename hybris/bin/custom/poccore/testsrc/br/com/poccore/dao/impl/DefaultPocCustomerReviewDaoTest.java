package br.com.poccore.dao.impl;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerReviewDaoTest {

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
    public void testFindReviewRatingByReviewAndRater_validResult() {
        SearchResult<CustomerReviewRatingModel> searchResult = mock(SearchResult.class);
        doReturn(searchResult).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
        when(searchResult.getResult()).thenReturn(Collections.singletonList(mock(CustomerReviewRatingModel.class)));
        CustomerReviewRatingModel actual = dao.findReviewRatingByReviewAndRater(PK.fromLong(2L), PK.fromLong(1L));
        assertNotNull(actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testFindReviewRatingByReviewAndRater_nullResult() {
        doReturn(mock(SearchResult.class)).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
        CustomerReviewRatingModel actual = dao.findReviewRatingByReviewAndRater(PK.fromLong(2L), PK.fromLong(1L));
        assertNull(actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }
}