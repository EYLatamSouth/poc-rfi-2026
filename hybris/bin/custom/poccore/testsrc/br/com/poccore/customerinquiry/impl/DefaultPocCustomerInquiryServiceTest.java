package br.com.poccore.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerInquiryServiceTest {

    @InjectMocks
    private DefaultPocCustomerInquiryService pocCustomerInquiryService;

    @Mock
    private ModelService modelService;

    @Mock
    private CommerceCommonI18NService commerceCommonI18NService;

    @Before
    public void setUp() {
        pocCustomerInquiryService.setModelService(modelService);
        pocCustomerInquiryService.setCommerceCommonI18NService(commerceCommonI18NService);
    }

    @Test
    public void testCreateCustomerInquiry() {
        CustomerProductInquiryModel inquiryModel = new CustomerProductInquiryModel();
        when(modelService.create(CustomerProductInquiryModel.class)).thenReturn(inquiryModel);

        ProductModel productModel = new ProductModel();
        CustomerModel customerModel = new CustomerModel();
        PocProductQuestionWsDTO questionWsDTO = new PocProductQuestionWsDTO();
        questionWsDTO.setQuestion("Dummy question?");
        Locale loc = Locale.of("BR");
        when(commerceCommonI18NService.getCurrentLocale()).thenReturn(loc);

        CustomerProductInquiryModel response = pocCustomerInquiryService.createCustomerInquiry(productModel, questionWsDTO, customerModel);
        verify(commerceCommonI18NService, times(1)).getCurrentLocale();
        assertNotNull(response);
        assertEquals(customerModel, response.getCustomer());
        assertEquals(productModel, response.getProduct());
        assertEquals(questionWsDTO.getQuestion(), response.getQuestion(loc));
        assertEquals(CustomerInquiryApprovalStatus.PENDING, response.getApprovalStatus());
    }
}
