package br.com.pocfacades.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPocCustomerInquiryFacadeTest {

    @InjectMocks
    private DefaultPocCustomerInquiryFacade pocCustomerInquiryFacade;

    @Mock
    private PocCustomerInquiryService pocCustomerInquiryService;

    @Mock
    private UserService userService;

    @Mock
    private Converter<CustomerProductInquiryModel, CustomerInquiryData> customerInquiryDataConverter;

    @Before
    public void setUp() {
        pocCustomerInquiryFacade.setPocCustomerInquiryService(pocCustomerInquiryService);
        pocCustomerInquiryFacade.setUserService(userService);
        pocCustomerInquiryFacade.setCustomerInquiryDataConverter(customerInquiryDataConverter);
    }

    @Test
    public void testWhenCustomerNotValid() {
        UserModel currentUser = mock(UserModel.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.isAnonymousUser(currentUser)).thenReturn(true);

        assertThrows(UsernameNotFoundException.class, () -> pocCustomerInquiryFacade.createCustomerInquiry("productCode", new PocProductQuestionWsDTO()));

        verify(pocCustomerInquiryService, never()).createCustomerInquiry(any());

        when(userService.isAnonymousUser(currentUser)).thenReturn(false);
        assertThrows(UsernameNotFoundException.class, () -> pocCustomerInquiryFacade.createCustomerInquiry("productCode", new PocProductQuestionWsDTO()));

        verify(pocCustomerInquiryService, never()).createCustomerInquiry(any());
    }

    @Test
    public void testCreateCustomerInquiry() {
        CustomerModel currentUser = mock(CustomerModel.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.isAnonymousUser(currentUser)).thenReturn(false);

        String productCode = "productCode";

        PocProductQuestionWsDTO wsDTO = new PocProductQuestionWsDTO();

        when(pocCustomerInquiryService.createCustomerInquiry(any())).thenReturn(mock(CustomerProductInquiryModel.class));
        when(customerInquiryDataConverter.convert(any(CustomerProductInquiryModel.class))).thenReturn(mock(CustomerInquiryData.class));

        CustomerInquiryData response = pocCustomerInquiryFacade.createCustomerInquiry(productCode, wsDTO);

        assertNotNull(response);

        verify(userService, times(1)).getCurrentUser();
        verify(userService, times(1)).isAnonymousUser(currentUser);
        verify(pocCustomerInquiryService, times(1)).createCustomerInquiry(any());
        verify(customerInquiryDataConverter, times(1)).convert(any(CustomerProductInquiryModel.class));
    }
}
