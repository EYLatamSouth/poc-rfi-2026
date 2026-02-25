package br.com.poccore.dao;

import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

public interface PocReviewDao {
    SearchPageData<CustomerReviewModel> findNthProductReview(String productCode, int nth);

    CustomerReviewRatingModel findReviewRatingByReviewAndRater(PK reviewPk, PK raterPk);
}
