package br.com.poccore.service.impl;

import br.com.poccore.PocUtil;
import br.com.poccore.dao.PocCustomerReviewDao;
import br.com.poccore.model.CustomerReviewRatingModel;
import br.com.poccore.service.PocCustomerReviewService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultPocCustomerReviewService implements PocCustomerReviewService {
    private final Logger log = LoggerFactory.getLogger(DefaultPocCustomerReviewService.class);

    private ModelService modelService;
    private PocCustomerReviewDao pocCustomerReviewDao;

    /**
     * Finds a CustomerReviewModel for given productCode based on its review ID.
     * Uses to {@link PocCustomerReviewDao} to search the review with the provided information, then validates the returned content.
     *
     * @param productCode The code for the target product.
     * @param reviewId    The Review's ID (Primary Key) value.
     * @return single {@link CustomerReviewModel} found
     * @throws IllegalArgumentException     if the search result is null
     * @throws UnknownIdentifierException   if the search result is empty
     * @throws AmbiguousIdentifierException if the search result contains more than one item
     */
    @Override
    public CustomerReviewModel getProductReviewById(String productCode, String reviewId) {
        log.info("Searching for {} Customer Review for product {}", reviewId, productCode);
        CustomerReviewModel result = getPocCustomerReviewDao().findProductReviewById(productCode, reviewId);
        ServicesUtil.validateParameterNotNull(
            result,
            "No review was found for given product code and review ID."
        );
        return result;
    }

    /**
     * Find a previously created {@link CustomerReviewRatingModel} by the rater customer.
     *
     * @param review    The target review.
     * @param rater     The rater customer.
     * @return a {@link CustomerReviewRatingModel} previously created by the rater customer.
     */
    @Override
    public CustomerReviewRatingModel findCustomerReviewRating(CustomerReviewModel review, CustomerModel rater) {
        return getPocCustomerReviewDao().findReviewRatingByReviewAndRater(review.getPk(), rater.getPk());
    }

    /**
     * Create a Product Review Rating or Updates an existing one.
     * Will use the productCode and its review ID to find the targeted review, and create a {@link CustomerReviewRatingModel}
     * to save is helpfulness link both the creator customer (the one that created the CustomerReview) and the rating
     * customer (the one that rated the review). If a {@link CustomerReviewRatingModel} for the given information was found,
     * will update its value instead
     *
     * @param productCode The code for the target product.
     * @param reviewId    The Review's ID (Primary Key) value.
     * @param helpful     The review helpfulness.
     * @throws IllegalArgumentException if the user from the {@link CustomerReviewModel} is null
     * @throws IllegalStateException    if the rating user is not a {@link CustomerModel}
     */
    @Override
    public CustomerReviewRatingModel createProductReviewRating(
        UserModel ratingUser,
        String productCode,
        String reviewId,
        boolean helpful
    ) throws IllegalArgumentException, IllegalStateException {
        log.info("Creating Customer Review Rate for {} Customer Review for product {}", reviewId, productCode);
        CustomerReviewModel review = getProductReviewById(productCode, reviewId);
        ServicesUtil.validateParameterNotNull(review.getUser(), String.format("Review %s does not contain User.", review));
        PocUtil.validateParameterType(ratingUser, CustomerModel.class);

        CustomerReviewRatingModel reviewRate = this.findCustomerReviewRating(review, (CustomerModel) ratingUser);
        if (Objects.isNull(reviewRate)) {
            reviewRate = getModelService().create(CustomerReviewRatingModel.class);
            reviewRate.setCustomerReview(review);
            reviewRate.setCustomer((CustomerModel) ratingUser);
            reviewRate.setIsUseful(helpful);
            getModelService().save(reviewRate);
            getModelService().refresh(reviewRate);
            log.info("Created Customer Review Rate {}", reviewRate);
        } else if (helpful != reviewRate.getIsUseful()) {
            reviewRate.setIsUseful(helpful);
            getModelService().save(reviewRate);
            getModelService().refresh(reviewRate);
            log.info("Updated Customer Review Rate {}", reviewRate);
        } else {
            log.warn("No operation was done to Customer Review Rate {}", reviewRate);
        }

        return reviewRate;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public PocCustomerReviewDao getPocCustomerReviewDao() {
        return pocCustomerReviewDao;
    }

    public void setPocCustomerReviewDao(PocCustomerReviewDao pocCustomerReviewDao) {
        this.pocCustomerReviewDao = pocCustomerReviewDao;
    }
}
