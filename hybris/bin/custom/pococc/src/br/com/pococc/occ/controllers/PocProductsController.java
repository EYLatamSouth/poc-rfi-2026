package br.com.pococc.occ.controllers;

import br.com.poc.occ.dto.product.PocProductEngagementSummaryWsDTO;
import br.com.pocfacades.product.PocProductFacade;
import br.com.pocfacades.review.PocReviewFacade;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservices.core.product.data.ReviewDataList;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewListWsDTO;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@Tag(name = "Poc Products")
@RequestMapping(value = "/{baseSiteId}/products")
public class PocProductsController extends PocBaseController
{
    private static final Logger LOG = LoggerFactory.getLogger(PocProductsController.class);

    private static final String PRINCIPAL_ANONYMOUS_KEY = "toggle.product.review.principal.anonymization.enabled";

    @Resource(name = "cwsProductFacade")
    private ProductFacade productFacade;

    @Resource(name = "pocProductFacade")
    private PocProductFacade pocProductFacade;


    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "pocReviewFacade")
    private PocReviewFacade pocReviewFacade;

    @GetMapping("/{productCode}/reviews")
    @RequestMappingOverride(priorityProperty = "pococc.PocProductsController.getProductReviews.priority")
    @ResponseBody
    @Operation(operationId = "getProductReviews", summary = "Retrieves the reviews of a product.", description = "Retrieves all the reviews for a product. To limit the number of reviews returned, use the maxCount parameter.")
    @ApiBaseSiteIdParam
    public ReviewListWsDTO getProductReviews(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
            @Parameter(description = "Maximum number of reviews.") @RequestParam(required = false) final Integer maxCount,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
    {
        LOG.info("POC CUSTOM GET PRODUCT REVIEWS");
        final ReviewDataList reviewDataList = new ReviewDataList();
        reviewDataList.setReviews(productFacade.getReviews(productCode, maxCount));
        if (configurationService.getConfiguration().getBoolean(PRINCIPAL_ANONYMOUS_KEY, true))
        {
        //    vivoPocProductsHelper.anonymizeReviewPrincipal(reviewDataList);
        }
        return getDataMapper().map(reviewDataList, ReviewListWsDTO.class, fields);
    }

    /**
     * Creates and updates a Customer Review Rating for given product.
     *
     * @param productCode   The code for the target product.
     * @param id            The chronological position.
     * @param helpful       Review rate value.
     * @return HttpStatus 201 to created customer review rating.
     */
    @Secured({"ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @PostMapping("/{productCode}/review/{id}/helpful")
    @ResponseBody
    @Operation(operationId = "postReviewRating", summary = "Rate a review helpability.", description = "Rate a review if it as helpful or not.")
    @ApiBaseSiteIdParam
    public ResponseEntity<Object> postReviewRating(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
            @Parameter(description = "Review Id.", required = true) @PathVariable final String id,
            @RequestParam(defaultValue = "true") final boolean helpful)
    {
        pocReviewFacade.createProductReviewRating(productCode, Integer.parseInt(id), helpful);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{productCode}/engagementSummary")
    @ResponseBody
    @Operation(
        operationId = "getProductEngagementSummary",
        summary = "Retrieves the Engagement Summary of a product.",
        description = "Retrieves an Engagement Summary AVG for a product."
    )
    @ApiBaseSiteIdParam
    public PocProductEngagementSummaryWsDTO getProductEngagementSummary(
            @Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode)
    {
        LOG.info("POC CUSTOM GET PRODUCT Engagement Summary");
        PocProductEngagementSummaryInfoData pocProductEngagementSummary =
            pocProductFacade.getEngagementSummary(productCode);
        return getDataMapper().map(pocProductEngagementSummary, PocProductEngagementSummaryWsDTO.class);
    }
}
