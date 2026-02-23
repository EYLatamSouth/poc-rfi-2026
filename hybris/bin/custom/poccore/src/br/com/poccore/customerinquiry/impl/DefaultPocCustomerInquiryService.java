package br.com.poccore.customerinquiry.impl;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.enums.ApprovalStatus;
import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class DefaultPocCustomerInquiryService implements PocCustomerInquiryService {

    private ModelService modelService;

    @Override
    public CustomerProductInquiryModel createCustomerInquiry(ProductModel productModel, PocProductQuestionWsDTO questionWsDTO, CustomerModel customerModel) {
        CustomerProductInquiryModel inquiryModel = getModelService().create(CustomerProductInquiryModel.class);

        inquiryModel.setCustomer(customerModel);
        inquiryModel.setProduct(productModel);
        inquiryModel.setQuestion(questionWsDTO.getQuestion());
        inquiryModel.setApprovalStatus(ApprovalStatus.PENDING);

        getModelService().save(inquiryModel);

        return inquiryModel;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
