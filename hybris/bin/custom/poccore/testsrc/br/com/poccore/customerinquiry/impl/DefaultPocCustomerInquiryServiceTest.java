package br.com.poccore.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerInquiryServiceTest {

    @InjectMocks
    private DefaultPocCustomerInquiryService pocCustomerInquiryService;

    @Mock
    private ModelService modelService;

    @Before
    public void setUp() {
        pocCustomerInquiryService.setModelService(modelService);
    }

    @Test
    public void testCreateCustomerInquiry() {
        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        when(modelService.create(CustomerProductInquiryModel.class)).thenReturn(inquiryModel);

        ProductModel productModel = new ProductModel();
        CustomerModel customerModel = new CustomerModel();
        PocProductQuestionWsDTO questionWsDTO = new PocProductQuestionWsDTO();
        questionWsDTO.setQuestion("Dummy question?");

        CustomerProductInquiryModel response = pocCustomerInquiryService.createCustomerInquiry(productModel, questionWsDTO, customerModel);

        assertNotNull(response);
        verify(response, times(1)).setCustomer(customerModel);
        verify(response, times(1)).setProduct(productModel);
        verify(response, times(1)).setQuestion(questionWsDTO.getQuestion());
        verify(response, times(1)).setApprovalStatus(CustomerInquiryApprovalStatus.PENDING);
    }
}
