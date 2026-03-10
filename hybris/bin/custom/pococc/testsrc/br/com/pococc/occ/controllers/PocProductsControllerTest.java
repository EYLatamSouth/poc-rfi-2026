package br.com.pococc.occ.controllers;

import br.com.poc.occ.dto.product.PocProductEngagementSummaryWsDTO;
import br.com.pocfacades.product.PocProductFacade;
import br.com.pocfacades.review.PocCustomerReviewFacade;
import br.com.pococc.occ.validators.PocCustomerReviewRatingValidator;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
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
    private PocCustomerReviewFacade pocCustomerReviewFacade;

    @Mock
    private PocCustomerReviewRatingValidator pocReviewRatingValidator;

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
	
	@Test
	public void testPostReviewRating() {
		String productCode = "PRODUCT";
		Integer index = 0;
		boolean helpful = false;

		doNothing().when(pocReviewRatingValidator).validate(any(), any());
		doNothing().when(pocCustomerReviewFacade).createProductReviewRating(anyString(), anyInt(), anyBoolean());

		ResponseEntity response = pocProductsController.postReviewRating(productCode, index, helpful);

		verify(pocReviewRatingValidator, times(1)).validate(any(), any());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
}