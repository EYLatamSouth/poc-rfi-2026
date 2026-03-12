package br.com.poccore.dao.impl;

import br.com.poccore.dao.PocCustomerReviewDao;
import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.util.CollectionUtils;

public class DefaultPocCustomerReviewDao implements PocCustomerReviewDao {

    private static final String FIND_PRODUCT_REVIEW_BY_ID = """
            SELECT {cr.pk}
            FROM {CustomerReview AS cr
            JOIN Product AS pd ON {pd.pk}={cr.product}}
            WHERE {pd.code} = ?productCode
            AND {cr.pk} = ?reviewId
            """;
    private static final String FIND_REVIEW_RATING_BY_REVIEW_AND_RATER = """
            SELECT {crr.pk}
            FROM {CustomerReviewRating AS crr}
            WHERE {crr.customerReview} = ?customerReview
            AND {crr.customer} = ?customer
            """;

    private FlexibleSearchService flexibleSearchService;

    /**
     * Searches for a CustomerReviewModel for given productCode based on its ID.
     * The method will filter all Customer Reviews for the provided product code.
     *
     * @param productCode   The target product code
     * @param reviewId      The Review's ID (Primary Key) value.
     * @return The search result for a single {@Link CustomerReviewModel}
     */
    @Override
    public CustomerReviewModel findProductReviewById(String productCode, String reviewId) {
        FlexibleSearchQuery fquery = new FlexibleSearchQuery(FIND_PRODUCT_REVIEW_BY_ID);
        fquery.addQueryParameter("productCode", productCode);
        fquery.addQueryParameter("reviewId", reviewId);

        SearchResult<CustomerReviewModel> result = getFlexibleSearchService().search(fquery);
        if (CollectionUtils.isEmpty(result.getResult())) return null;
        return result.getResult().getFirst();
    }

    /**
     * Searches for a {@link CustomerReviewRatingModel} by the review and customer.
     *
     * @param reviewPk  PK from the review.
     * @param raterPk   PK from rater customer.
     * @return {@link CustomerReviewRatingModel} from given information.
     */
    @Override
    public CustomerReviewRatingModel findReviewRatingByReviewAndRater(PK reviewPk, PK raterPk) {
        FlexibleSearchQuery fquery = new FlexibleSearchQuery(FIND_REVIEW_RATING_BY_REVIEW_AND_RATER);
        fquery.addQueryParameter("customerReview", reviewPk);
        fquery.addQueryParameter("customer", raterPk);

        SearchResult<CustomerReviewRatingModel> result = getFlexibleSearchService().search(fquery);
        if (CollectionUtils.isEmpty(result.getResult())) return null;
        return result.getResult().getFirst();
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
