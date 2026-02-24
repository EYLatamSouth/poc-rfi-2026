package br.com.pocfacades.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    private ProductService productService;

    @Before
    public void setUp() {
        pocCustomerInquiryFacade.setPocCustomerInquiryService(pocCustomerInquiryService);
        pocCustomerInquiryFacade.setUserService(userService);
        pocCustomerInquiryFacade.setProductService(productService);
    }

    @Test
    public void testWhenCustomerNotValid() {
        UserModel currentUser = mock(UserModel.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.isAnonymousUser(currentUser)).thenReturn(true);

        assertThrows(UsernameNotFoundException.class, () -> pocCustomerInquiryFacade.createCustomerInquiry("productCode", new PocProductQuestionWsDTO()));

        verify(productService, never()).getProductForCode(anyString());
        verify(pocCustomerInquiryService, never()).createCustomerInquiry(any(), any(), any());

        when(userService.isAnonymousUser(currentUser)).thenReturn(false);
        assertThrows(UsernameNotFoundException.class, () -> pocCustomerInquiryFacade.createCustomerInquiry("productCode", new PocProductQuestionWsDTO()));

        verify(productService, never()).getProductForCode(anyString());
        verify(pocCustomerInquiryService, never()).createCustomerInquiry(any(), any(), any());
    }

    @Test
    public void testCreateCustomerInquiry() {
        CustomerModel currentUser = mock(CustomerModel.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.isAnonymousUser(currentUser)).thenReturn(false);

        String productCode = "productCode";
        ProductModel productModel = mock(ProductModel.class);
        when(productService.getProductForCode(productCode)).thenReturn(productModel);

        PocProductQuestionWsDTO wsDTO = new PocProductQuestionWsDTO();

        pocCustomerInquiryFacade.createCustomerInquiry(productCode, wsDTO);

        verify(userService, times(1)).getCurrentUser();
        verify(userService, times(1)).isAnonymousUser(currentUser);
        verify(productService, times(1)).getProductForCode(productCode);
        verify(pocCustomerInquiryService, times(1)).createCustomerInquiry(productModel, wsDTO, currentUser);
    }
}
