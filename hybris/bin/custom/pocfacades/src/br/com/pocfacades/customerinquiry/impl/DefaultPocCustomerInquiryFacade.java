package br.com.pocfacades.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.customerinquiry.PocCustomerInquiryFacade;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static br.com.pocfacades.constants.PocfacadesConstants.USER_NOT_CUSTOMER_MESSAGE;

public class DefaultPocCustomerInquiryFacade implements PocCustomerInquiryFacade {

    private PocCustomerInquiryService pocCustomerInquiryService;
    private UserService userService;
    private Converter<CustomerProductInquiryModel, CustomerInquiryData> customerInquiryDataConverter;

    @Override
    public CustomerInquiryData createCustomerInquiry(String productCode, PocProductQuestionWsDTO questionWsDTO) {
        UserModel userModel = getUserService().getCurrentUser();
        if(isValidCustomer(userModel)) {
            CustomerInquiryData inquiryData = new CustomerInquiryData();
            inquiryData.setProduct(productCode);
            inquiryData.setQuestion(questionWsDTO.getQuestion());

            CustomerProductInquiryModel model = getPocCustomerInquiryService().createCustomerInquiry(inquiryData);

            return getCustomerInquiryDataConverter().convert(model);
        }

        throw new UsernameNotFoundException(String.format(USER_NOT_CUSTOMER_MESSAGE, userModel.getUid()));
    }

    private boolean isValidCustomer(UserModel userModel) {
        return !getUserService().isAnonymousUser(userModel) && userModel instanceof CustomerModel;
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

    public Converter<CustomerProductInquiryModel, CustomerInquiryData> getCustomerInquiryDataConverter() {
        return customerInquiryDataConverter;
    }

    public void setCustomerInquiryDataConverter(Converter<CustomerProductInquiryModel, CustomerInquiryData> customerInquiryDataConverter) {
        this.customerInquiryDataConverter = customerInquiryDataConverter;
    }
}
