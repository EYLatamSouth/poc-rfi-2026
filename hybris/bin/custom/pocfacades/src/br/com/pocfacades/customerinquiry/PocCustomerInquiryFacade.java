package br.com.pocfacades.customerinquiry;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;

public interface PocCustomerInquiryFacade {

    void createCustomerInquiry(String productCode, PocProductQuestionWsDTO questionWsDTO);

}
