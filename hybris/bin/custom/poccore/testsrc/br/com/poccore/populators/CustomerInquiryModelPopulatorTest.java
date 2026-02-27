package br.com.poccore.populators;

import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerInquiryModelPopulatorTest {

    @InjectMocks
    private CustomerInquiryModelPopulator customerInquiryModelPopulator;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Before
    public void setUp() {
        customerInquiryModelPopulator.setUserService(userService);
        customerInquiryModelPopulator.setProductService(productService);
    }

    @Test
    public void testWhenNotCustomer() {
        CustomerInquiryData source = new CustomerInquiryData();
        source.setQuestion("What is the warranty period for this product?");
        source.setProduct("product123");

        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        ProductModel productModel = new ProductModel();
        when(productService.getProductForCode(source.getProduct())).thenReturn(productModel);

        customerInquiryModelPopulator.populate(source, inquiryModel);

        verify(inquiryModel, never()).setCustomer(any());
        verify(inquiryModel, times(1)).setQuestion(source.getQuestion());
        verify(inquiryModel, times(1)).setApprovalStatus(CustomerInquiryApprovalStatus.PENDING);
        verify(inquiryModel, times(1)).setProduct(productModel);
    }

    @Test
    public void testPopulate() {
        CustomerInquiryData source = new CustomerInquiryData();
        source.setQuestion("What is the warranty period for this product?");
        source.setProduct("product123");

        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        ProductModel productModel = new ProductModel();
        when(productService.getProductForCode(source.getProduct())).thenReturn(productModel);
        CustomerModel customerModel = new CustomerModel();
        when(userService.getCurrentUser()).thenReturn(customerModel);

        customerInquiryModelPopulator.populate(source, inquiryModel);

        verify(inquiryModel, times(1)).setCustomer(customerModel);
        verify(inquiryModel, times(1)).setQuestion(source.getQuestion());
        verify(inquiryModel, times(1)).setApprovalStatus(CustomerInquiryApprovalStatus.PENDING);
        verify(inquiryModel, times(1)).setProduct(productModel);
    }
}
