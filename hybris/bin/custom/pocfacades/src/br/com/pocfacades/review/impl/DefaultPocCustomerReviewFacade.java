package br.com.pocfacades.review.impl;

import br.com.poccore.service.PocCustomerReviewService;
import br.com.pocfacades.review.PocCustomerReviewFacade;
import de.hybris.platform.servicelayer.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultPocCustomerReviewFacade implements PocCustomerReviewFacade {
    private final Logger log = LoggerFactory.getLogger(DefaultPocCustomerReviewFacade.class);

    private PocCustomerReviewService pocReviewService;
    private UserService userService;


    /**
     * Will call the PocReviewService to create a ProductReviewRating.
     *
     * @param productCode   The code for the target product.
     * @param reviewId      The Review's ID (Primary Key) value.
     * @param helpful       Review rate value.
     *
     * @throws IllegalArgumentException when productCode or reviewId doesn't follow requirements
     */
    @Override
    public void createProductReviewRating(String productCode, String reviewId, boolean helpful) throws IllegalArgumentException {
        try {
            getPocReviewService().createProductReviewRating(getUserService().getCurrentUser(), productCode, reviewId, helpful);
        }
        catch (Exception e) {
            log.error("Error occurred while creating CustomerReviewRating: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PocCustomerReviewService getPocReviewService() {
        return pocReviewService;
    }

    public void setPocReviewService(PocCustomerReviewService pocReviewService) {
        this.pocReviewService = pocReviewService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}