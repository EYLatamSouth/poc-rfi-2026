package br.com.poccore.dao.impl;

import br.com.poccore.model.FeatureFlagModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultFeatureFlagDaoTest {

    @Mock
    private FlexibleSearchService flexibleSearchService;

    @InjectMocks
    private DefaultFeatureFlagDao defaultFeatureFlagDao;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindFeatureFlagByKey_ReturnsResult() {
        String key = "testKey";
        FeatureFlagModel featureFlag = new FeatureFlagModel();
        List<FeatureFlagModel> featureFlagList = Collections.singletonList(featureFlag);

        SearchResult searchResult = mock(SearchResult.class);
        when(searchResult.getResult()).thenReturn(featureFlagList);
        when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);

        List<FeatureFlagModel> result = defaultFeatureFlagDao.findFeatureFlagByKey(key);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(featureFlag, result.get(0));

        ArgumentCaptor<FlexibleSearchQuery> queryCaptor = ArgumentCaptor.forClass(FlexibleSearchQuery.class);
        verify(flexibleSearchService).search(queryCaptor.capture());
        FlexibleSearchQuery capturedQuery = queryCaptor.getValue();
        assertTrue(capturedQuery.getQuery().contains("SELECT {ff.pk}"));
        assertEquals(key, capturedQuery.getQueryParameters().get("key"));
    }

    @Test
    public void testFindFeatureFlagByKey_ReturnsNullWhenEmpty() {
        String key = "nonExistingKey";

        SearchResult searchResult = mock(SearchResult.class);
        when(searchResult.getResult()).thenReturn(Collections.emptyList());
        when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);

        List<FeatureFlagModel> result = defaultFeatureFlagDao.findFeatureFlagByKey(key);

        assertNull(result);

        verify(flexibleSearchService).search(any(FlexibleSearchQuery.class));
    }
}