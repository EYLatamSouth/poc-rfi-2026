package br.com.poccore.customerinquiry.impl;

import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
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

    @Mock
    private Converter<CustomerInquiryData, CustomerProductInquiryModel> customerInquiryConverter;


    @Before
    public void setUp() {
        pocCustomerInquiryService.setModelService(modelService);
        pocCustomerInquiryService.setCustomerInquiryModelConverter(customerInquiryConverter);
    }

    @Test
    public void testCreateCustomerInquiry() {
        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        when(modelService.create(CustomerProductInquiryModel.class)).thenReturn(inquiryModel);

        CustomerInquiryData data = new CustomerInquiryData();
        data.setQuestion("Dummy question?");

        CustomerProductInquiryModel response = pocCustomerInquiryService.createCustomerInquiry(data);
        assertNotNull(response);
        verify(modelService, times(1)).create(CustomerProductInquiryModel.class);
        verify(customerInquiryConverter, times(1)).convert(any(CustomerInquiryData.class), eq(inquiryModel));
    }
}
