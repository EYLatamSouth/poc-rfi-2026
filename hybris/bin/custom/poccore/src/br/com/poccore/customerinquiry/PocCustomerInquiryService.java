package br.com.poccore.customerinquiry;

import br.com.poccore.model.CustomerProductInquiryModel;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;

public interface PocCustomerInquiryService {

    CustomerProductInquiryModel createCustomerInquiry(CustomerInquiryData inquiryData);

}
