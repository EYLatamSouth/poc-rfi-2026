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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
        CustomerProductInquiryModel inquiryModel = new CustomerProductInquiryModel();
        when(modelService.create(CustomerProductInquiryModel.class)).thenReturn(inquiryModel);

        ProductModel productModel = new ProductModel();
        CustomerModel customerModel = new CustomerModel();
        PocProductQuestionWsDTO questionWsDTO = new PocProductQuestionWsDTO();
        questionWsDTO.setQuestion("Dummy question?");

        CustomerProductInquiryModel response = pocCustomerInquiryService.createCustomerInquiry(productModel, questionWsDTO, customerModel);
        assertNotNull(response);
        assertEquals(customerModel, response.getCustomer());
        assertEquals(productModel, response.getProduct());
        assertEquals(questionWsDTO.getQuestion(), response.getQuestion());
        assertEquals(CustomerInquiryApprovalStatus.PENDING, response.getApprovalStatus());
    }
}
