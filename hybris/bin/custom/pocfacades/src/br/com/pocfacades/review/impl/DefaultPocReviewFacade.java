package br.com.pocfacades.review.impl;

import br.com.poccore.PocUtil;
import br.com.poccore.model.CustomerReviewRatingModel;
import br.com.poccore.service.PocReviewService;
import br.com.pocfacades.review.PocReviewFacade;
import com.google.common.base.Preconditions;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultPocReviewFacade implements PocReviewFacade {
    private final Logger LOG = LoggerFactory.getLogger(DefaultPocReviewFacade.class);

    private PocReviewService pocReviewService;

    /**
     * Will call the PocReviewService to create a ProductReviewRating.
     *
     * @param productCode   The code for the target product.
     * @param nth           The chronological position.
     * @param helpful       Review rate value.
     *
     * @throws IllegalArgumentException when productCode or nth doesn't follow requirements
     */
    @Override
    public void createProductReviewRating(String productCode, int nth, boolean helpful) throws IllegalArgumentException {
        try {
            PocUtil.validateStringValues(productCode, "productCode");
            Preconditions.checkArgument(nth > 0, "Nth cannot be less than zero.");
            getPocReviewService().createProductReviewRating(productCode, nth, helpful);
        }
        catch (Exception e) {
            LOG.error("Error occurred while creating CustomerReviewRating: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PocReviewService getPocReviewService() {
        return pocReviewService;
    }

    public void setPocReviewService(PocReviewService pocReviewService) {
        this.pocReviewService = pocReviewService;
    }
}
