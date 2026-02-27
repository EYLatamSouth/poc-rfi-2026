package br.com.poccore.populators;

import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.user.UserService;

public class CustomerInquiryModelPopulator implements Populator<CustomerInquiryData, CustomerProductInquiryModel> {

    private UserService userService;
    private ProductService productService;

    @Override
    public void populate(CustomerInquiryData source, CustomerProductInquiryModel target) {
        UserModel userModel = getUserService().getCurrentUser();
        if(userModel instanceof CustomerModel customerModel) {
            target.setCustomer(customerModel);
        }
        target.setProduct(getProductService().getProductForCode(source.getProduct()));
        target.setQuestion(source.getQuestion());
        target.setApprovalStatus(CustomerInquiryApprovalStatus.PENDING);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
