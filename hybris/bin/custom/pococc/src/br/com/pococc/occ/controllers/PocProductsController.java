package br.com.pococc.occ.controllers;

import br.com.poc.occ.dto.product.PocProductEngagementSummaryWsDTO;
import br.com.poccore.annotation.FeatureFlagRestriction;
import br.com.pocfacades.product.PocProductFacade;
import br.com.poc.occ.dto.product.PocReviewRatingData;
import br.com.pocfacades.review.PocCustomerReviewFacade;
import br.com.pococc.occ.validators.PocCustomerReviewRatingValidator;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewWsDTO;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import static br.com.pococc.occ.constants.PococcConstants.*;


@Controller
@Tag(name = "Poc Products")
@RequestMapping(value = "/{baseSiteId}/products")
public class PocProductsController extends PocBaseController {
    private static final Logger LOG = LoggerFactory.getLogger(PocProductsController.class);

    @Resource(name = "cwsProductFacade")
    private ProductFacade productFacade;

    @Resource(name = "pocProductFacade")
    private PocProductFacade pocProductFacade;

    @Resource(name = "pocReviewFacade")
    private PocCustomerReviewFacade pocCustomerReviewFacade;

    @Resource(name = "pocReviewRatingValidator")
    private PocCustomerReviewRatingValidator pocReviewRatingValidator;

    @Resource(name = "reviewDTOValidator")
    private Validator reviewDTOValidator;

    /**
     * Creates and updates a Customer Review Rating for given product.
     *
     * @param productCode The code for the target product.
     * @param id          The Review's ID (Primary Key) value.
     * @param helpful     Review rate value.
     * @return HttpStatus 201 to created customer review rating.
     */
    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
    @PostMapping("/{productCode}/review/{id}/helpful")
    @ResponseBody
    @Operation(operationId = "postReviewRating", summary = "Rate a review helpability.", description = "Rate a review if it as helpful or not.")
    @ApiBaseSiteIdParam
    @FeatureFlagRestriction(name = US_02)
    public ResponseEntity<Void> postReviewRating(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
            @Parameter(description = "Review Id.", required = true) @PathVariable final String id,
            @RequestParam(defaultValue = "true") final boolean helpful) {
        PocReviewRatingData validateData = new PocReviewRatingData();
        validateData.setId(id);
        validateData.setProductCode(productCode);
        validate(validateData, "PocReviewRatingData", pocReviewRatingValidator);
        pocCustomerReviewFacade.createProductReviewRating(productCode, id, helpful);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Generates a summary report of the engagement statistics for given product.
     *
     * @param productCode The code for the target product.
     * @return HttpStatus 200 for successfully generated report.
     */
    @Secured({ "ROLE_EMPLOYEEGROUP", "ROLE_TRUSTED_CLIENT" })
    @GetMapping("/{productCode}/engagementSummary")
    @ResponseBody
    @Operation(
            operationId = "getProductEngagementSummary",
            summary = "Retrieves the Engagement Summary of a product.",
            description = "Retrieves an Engagement Summary AVG for a product."
    )
    @ApiBaseSiteIdParam
    @FeatureFlagRestriction(name = US_05)
    public PocProductEngagementSummaryWsDTO getProductEngagementSummary(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode) {
        LOG.info("POC CUSTOM GET PRODUCT Engagement Summary");
        PocProductEngagementSummaryInfoData pocProductEngagementSummary =
                pocProductFacade.getEngagementSummary(productCode);
        return getDataMapper().map(pocProductEngagementSummary, PocProductEngagementSummaryWsDTO.class);
    }

    /**
     * Creates a Customer Review for given product.
     *
     * @param productCode The code of the target product.
     * @param review      The review content - headline, comment and rating for the target product.
     * @param fields      Optional - The fields that will be returned from the response object.
     * @return HttpStatus 201 and ReviewWsDTO of the created information.
     */
    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
    @PostMapping(value = "/{productCode}/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @RequestMappingOverride(priorityProperty = "pococc.PocProductsController.CreateProductReviews.priority")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Operation(operationId = "createProductReview", summary = "Creates a customer review as an anonymous or authenticated user.", description = "Creates a customer review for a product as an anonymous or authenticate user.")
    @ApiBaseSiteIdParam
    @FeatureFlagRestriction(name = US_01)
    public ReviewWsDTO createProductReview(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
            @Parameter(description = "Object contains review details like : rating, alias, headline, comment.", required = true) @RequestBody final ReviewWsDTO review,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {

        validate(review, "review", reviewDTOValidator);
        final ReviewData reviewData = getDataMapper().map(review, ReviewData.class, "alias,rating,headline,comment");
        final ReviewData reviewDataRet = productFacade.postReview(productCode, reviewData);
        return getDataMapper().map(reviewDataRet, ReviewWsDTO.class, fields);
    }

}
