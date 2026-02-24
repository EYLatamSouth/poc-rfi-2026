package br.com.pocfacades.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.customerinquiry.PocCustomerInquiryFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultPocCustomerInquiryFacade implements PocCustomerInquiryFacade {

    private PocCustomerInquiryService pocCustomerInquiryService;
    private UserService userService;
    private ProductService productService;

    @Override
    public void createCustomerInquiry(String productCode, PocProductQuestionWsDTO questionWsDTO) {
        CustomerModel customerModel = getCurrentCustomer();
        ProductModel productModel = getProductService().getProductForCode(productCode);

        getPocCustomerInquiryService().createCustomerInquiry(productModel, questionWsDTO, customerModel);
    }

    private CustomerModel getCurrentCustomer() {
        UserModel userModel = getUserService().getCurrentUser();
        if(!getUserService().isAnonymousUser(userModel) && userModel instanceof CustomerModel customer) {
            return customer;
        }

        throw new UsernameNotFoundException(String.format("User with uid %s is not a customer", userModel.getUid()));
    }

    public PocCustomerInquiryService getPocCustomerInquiryService() {
        return pocCustomerInquiryService;
    }

    public void setPocCustomerInquiryService(PocCustomerInquiryService pocCustomerInquiryService) {
        this.pocCustomerInquiryService = pocCustomerInquiryService;
    }

    private UserService getUserService() {
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
