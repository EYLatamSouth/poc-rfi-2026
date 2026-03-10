package br.com.pocfacades.product.impl;

import br.com.poccore.service.PocProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultPocProductFacadeTest {
	@InjectMocks
	private DefaultPocProductFacade defaultPocProductFacade;

	@Mock
	private PocProductService pocProductService;

	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		defaultPocProductFacade.setPocProductService(pocProductService);
		defaultPocProductFacade.setSearchRestrictionService(searchRestrictionService);
	}

	@Test
	public void testGetEngagementSummary() {
		String productCode = "testProduct";
		defaultPocProductFacade.getEngagementSummary(productCode);
		verify(pocProductService, times(1)).getEngagementSummary(productCode);
	}
}