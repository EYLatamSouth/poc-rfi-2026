package br.com.poccore.populators;

import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerInquiryDataPopulatorTest {

    @InjectMocks
    private CustomerInquiryDataPopulator customerInquiryDataPopulator;

    @Before
    public void setUp() {}

    @Test
    public void testPopulateModelNullValues() {
        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        when(inquiryModel.getQuestion()).thenReturn("What is the warranty period for this product?");

        CustomerInquiryData data = new CustomerInquiryData();

        customerInquiryDataPopulator.populate(inquiryModel, data);


        assertEquals(inquiryModel.getQuestion(), data.getQuestion());
        assertNull(data.getCustomer());
        assertNull(data.getProduct());
        assertNull(data.getApprovalStatus());
    }

    @Test
    public void testPopulate() {
        CustomerProductInquiryModel inquiryModel = mock(CustomerProductInquiryModel.class);
        when(inquiryModel.getQuestion()).thenReturn("What is the warranty period for this product?");

        CustomerModel customerModel = new CustomerModel();
        customerModel.setUid("customer123");
        when(inquiryModel.getCustomer()).thenReturn(customerModel);

        ProductModel productModel = new ProductModel();
        productModel.setCode("product123");
        when(inquiryModel.getProduct()).thenReturn(productModel);

        when(inquiryModel.getApprovalStatus()).thenReturn(CustomerInquiryApprovalStatus.PENDING);

        CustomerInquiryData data = new CustomerInquiryData();

        customerInquiryDataPopulator.populate(inquiryModel, data);


        assertEquals(inquiryModel.getQuestion(), data.getQuestion());
        assertEquals(customerModel.getUid(), data.getCustomer());
        assertEquals(productModel.getCode(), data.getProduct());
        assertEquals(inquiryModel.getApprovalStatus().getCode(), data.getApprovalStatus());
    }
}
