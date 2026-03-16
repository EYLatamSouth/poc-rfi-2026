package br.com.poccore.dao;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocCustomerReviewDao {
    CustomerReviewModel findProductReviewById(String productCode, String reviewId);

    CustomerReviewRatingModel findReviewRatingByReviewAndRater(PK reviewPk, PK raterPk);
}
