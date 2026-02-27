package br.com.poccore.customerinquiry.impl;

import br.com.poccore.customerinquiry.PocCustomerInquiryService;
import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

public class DefaultPocCustomerInquiryService implements PocCustomerInquiryService {

    private ModelService modelService;
    private Converter<CustomerInquiryData, CustomerProductInquiryModel> customerInquiryModelConverter;

    @Override
    public CustomerProductInquiryModel createCustomerInquiry(CustomerInquiryData inquiryData) {
        CustomerProductInquiryModel inquiryModel = getModelService().create(CustomerProductInquiryModel.class);
        getCustomerInquiryModelConverter().convert(inquiryData, inquiryModel);

        getModelService().save(inquiryModel);

        return inquiryModel;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public Converter<CustomerInquiryData, CustomerProductInquiryModel> getCustomerInquiryModelConverter() {
        return customerInquiryModelConverter;
    }

    public void setCustomerInquiryModelConverter(Converter<CustomerInquiryData, CustomerProductInquiryModel> customerInquiryModelConverter) {
        this.customerInquiryModelConverter = customerInquiryModelConverter;
    }
}
