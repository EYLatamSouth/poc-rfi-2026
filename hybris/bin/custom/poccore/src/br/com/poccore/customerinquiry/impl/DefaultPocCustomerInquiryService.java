package br.com.poccore.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class DefaultPocCustomerInquiryService implements PocCustomerInquiryService {

    private ModelService modelService;
    private CommerceCommonI18NService commerceCommonI18NService;

    @Override
    public CustomerProductInquiryModel createCustomerInquiry(ProductModel productModel, PocProductQuestionWsDTO questionWsDTO, CustomerModel customerModel) {
        CustomerProductInquiryModel inquiryModel = getModelService().create(CustomerProductInquiryModel.class);

        inquiryModel.setCustomer(customerModel);
        inquiryModel.setProduct(productModel);
        inquiryModel.setQuestion(questionWsDTO.getQuestion());
        inquiryModel.setQuestion(questionWsDTO.getQuestion(), getCommerceCommonI18NService().getCurrentLocale());
        inquiryModel.setApprovalStatus(CustomerInquiryApprovalStatus.PENDING);

        getModelService().save(inquiryModel);

        return inquiryModel;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public CommerceCommonI18NService getCommerceCommonI18NService() {
        return commerceCommonI18NService;
    }

    public void setCommerceCommonI18NService(CommerceCommonI18NService commerceCommonI18NService) {
        this.commerceCommonI18NService = commerceCommonI18NService;
    }
}
