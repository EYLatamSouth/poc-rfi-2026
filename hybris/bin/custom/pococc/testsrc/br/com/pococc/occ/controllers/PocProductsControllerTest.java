package br.com.pococc.occ.controllers;

import br.com.poc.occ.dto.product.PocProductEngagementSummaryWsDTO;
import br.com.pocfacades.product.PocProductFacade;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Validator;

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
	private ProductFacade productFacade;

	@Mock
	private Validator reviewDTOValidator;

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
	public void testCreateProductReview() {
		String productCode = "productCode";
		ReviewWsDTO reviewWsDTO = new ReviewWsDTO();
		reviewWsDTO.setHeadline("headline");
		reviewWsDTO.setComment("comment");
		reviewWsDTO.setRating(4.0);
		doNothing().when(reviewDTOValidator).validate(any(), any());
		when(dataMapper.map(any(ReviewWsDTO.class), eq(ReviewData.class), any(String.class)))
			.thenReturn(new ReviewData());

		pocProductsController.createProductReview(productCode, reviewWsDTO, null);

		verify(productFacade, times(1)).postReview(eq(productCode), any(ReviewData.class));
	}
}