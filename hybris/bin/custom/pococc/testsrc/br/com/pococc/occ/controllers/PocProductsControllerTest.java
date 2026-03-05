package com.br.pococc.occ.controllers;

import br.com.poc.occ.dto.product.PocProductEngagementSummaryWsDTO;
import br.com.pocfacades.product.PocProductFacade;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PocProductsControllerTest {

	@InjectMocks
	private PocProductsController pocProductsController;

	@Mock
	private PocProductFacade pocProductFacade;

	@Mock
	private DataMapper dataMapper;

	@Test
	public void testGetProductEngagementSummary() {
		String productCode = "productCode";
		when(pocProductFacade.getEngagementSummary(productCode)).thenReturn(new PocProductEngagementSummaryInfoData());

		pocProductsController.getProductEngagementSummary(productCode);

		verify(pocProductFacade, times(1)).getEngagementSummary(productCode);
		verify(dataMapper, times(1))
			.map(any(PocProductEngagementSummaryInfoData.class), eq(PocProductEngagementSummaryWsDTO.class));
	}
}