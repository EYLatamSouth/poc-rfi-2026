package com.br.pococc.occ.controllers;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.pocfacades.customerinquiry.PocCustomerInquiryFacade;
import br.com.pocfacades.data.customerinquiry.CustomerInquiryData;
import com.br.pococc.occ.validators.PocProductQuestionValidator;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "Poc Users")
@RequestMapping(value = "/{baseSiteId}/users")
public class PocUsersController extends PocBaseController {

    @Resource(name = "pocProductQuestionValidator")
    private PocProductQuestionValidator pocProductQuestionValidator;

    @Resource(name = "pocCustomerInquiryFacade")
    private PocCustomerInquiryFacade pocCustomerInquiryFacade;

    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @PostMapping("/{userId}/products/{productCode}/question")
    @ResponseBody
    @Operation(operationId = "sendProductQuestion", summary = "Send Customer Question about the Product.", description = "Customer makes a question about the current product before buy")
    @ApiBaseSiteIdAndUserIdParam
    public ResponseEntity<?> sendProductQuestion(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
            @Parameter(description = "Customer question about the product.") @RequestBody final PocProductQuestionWsDTO questionWsDTO) {
        validate(questionWsDTO, "questionWsDTO", pocProductQuestionValidator);

        CustomerInquiryData data = pocCustomerInquiryFacade.createCustomerInquiry(productCode, questionWsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
