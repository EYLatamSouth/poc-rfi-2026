package br.com.poccore.populators;

import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import de.hybris.platform.converters.Populator;

public class CustomerInquiryDataPopulator implements Populator<CustomerProductInquiryModel, CustomerInquiryData> {

    @Override
    public void populate(CustomerProductInquiryModel source, CustomerInquiryData target) {
        if(source.getCustomer() != null) {
            target.setCustomer(source.getCustomer().getUid());
        }

        if(source.getProduct() != null) {
            target.setProduct(source.getProduct().getCode());
        }

        if(source.getApprovalStatus() != null) {
            target.setApprovalStatus(source.getApprovalStatus().getCode());
        }
        target.setQuestion(source.getQuestion());
    }
}
