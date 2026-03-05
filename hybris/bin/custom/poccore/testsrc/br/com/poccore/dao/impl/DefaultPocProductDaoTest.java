package br.com.poccore.dao.impl;

import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.customerreview.enums.CustomerReviewApprovalType.APPROVED;
import static de.hybris.platform.customerreview.enums.CustomerReviewApprovalType.REJECTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPocProductDaoTest {

    @InjectMocks
    private DefaultPocProductDao dao;

    @Mock
    private FlexibleSearchService flexibleSearchService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dao.setFlexibleSearchService(flexibleSearchService);
    }

    @Test
    public void testGetProductRatings() {
        SearchResult<Number> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(Collections.singletonList(3.45));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        List<Double> actual = dao.getProductRatings("productCode");
        assertNotNull(actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetVerifiedReviewRatingCount() {
        SearchResult<Integer> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(Collections.singletonList(5));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        int actual = dao.getVerifiedReviewRatingCount("productCode", Collections.singletonList(true));
        assertNotEquals(0, actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetRatingsWithUpvoteCount_withEqualHeadlines() {
        SearchResult<List<Object>> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(List.of(List.of("heading", 10), List.of("heading", 20)));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        Map<String, Integer> actual = dao.getRatingsWithUpvoteCount("productCode");

        assertNotNull(actual);
        assertEquals(1, actual.size());
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetRatingsWithUpvoteCount_withReviewsWithNoUpvotes() {
        SearchResult<List<Object>> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(List.of(List.of("heading", 10), List.of("heading1", 0)));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        Map<String, Integer> actual = dao.getRatingsWithUpvoteCount("productCode");

        assertNotNull(actual);
        assertEquals(1, actual.size());
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetReviewCountByStatus() {
        SearchResult<Integer> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(Collections.singletonList(8));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        int actual = dao.getReviewCountByStatus("productCode", List.of(APPROVED, REJECTED));
        assertNotEquals(0, actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetTotalApprovedQuestions() {
        SearchResult<Integer> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(Collections.singletonList(7));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        int actual = dao.getTotalApprovedQuestions("productCode");
        assertNotEquals(0, actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }


    @Test
    public void testGetQuestionCountByStatus() {
        SearchResult<Integer> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(Collections.singletonList(8));
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        int actual = dao.getQuestionCountByStatus(
            "productCode",
            List.of(CustomerInquiryApprovalStatus.APPROVED, CustomerInquiryApprovalStatus.UNAPPROVED)
        );
        assertNotEquals(0, actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }

    @Test
    public void testGetResponseTime() {
        SearchResult<List<Object>> searchResultMock = mock(SearchResult.class);
        when(searchResultMock.getResult()).thenReturn(new ArrayList<>());
        doReturn(searchResultMock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

        List<Duration> actual = dao.getResponseTime("productCode");
        assertNotNull(actual);
        verify(flexibleSearchService, times(1)).search(any(FlexibleSearchQuery.class));
    }
}