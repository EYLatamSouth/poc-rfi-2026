package br.com.pocfacades.customerinquiry;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;

public interface PocCustomerInquiryFacade {

    CustomerInquiryData createCustomerInquiry(String productCode, PocProductQuestionWsDTO questionWsDTO);

}
