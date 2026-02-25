package br.com.poccore.customerinquiry;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

public interface PocCustomerInquiryService {

    CustomerProductInquiryModel createCustomerInquiry(ProductModel productModel, PocProductQuestionWsDTO questionWsDTO, CustomerModel customerModel);

}
