package br.com.poccore.dao.impl;

import br.com.poccore.dao.PocCustomerReviewDao;
import br.com.poccore.model.CustomerReviewRatingModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import org.springframework.util.CollectionUtils;

public class DefaultPocCustomerReviewDao implements PocCustomerReviewDao {

    private static final String FIND_NTH_PRODUCT_REVIEW = "SELECT {cr.pk}" +
            " FROM {" + CustomerReviewModel._TYPECODE + " AS cr " +
            " JOIN " + ProductModel._TYPECODE + " AS pd ON {pd.pk}={cr.product}}" +
            " WHERE {pd." + ProductModel.CODE + "} = ?productCode" +
            " ORDER BY {cr." + CustomerReviewModel.CREATIONTIME + "} ASC";
    private static final String FIND_REVIEW_RATING_BY_REVIEW_AND_RATER = "SELECT {crr.pk}" +
            " FROM {" + CustomerReviewRatingModel._TYPECODE + " AS crr}" +
            " WHERE {crr.customerReview} = ?customerReview" +
            " AND {crr.customer} = ?customer";
    private static final int PAGE_SIZE = 1;

    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;
    private FlexibleSearchService flexibleSearchService;

    /**
     * Searches for a CustomerReviewModel for given productCode based on its chronological position.
     * The method will filter all Customer Reviews for the provides product code. Using the Pagination feature of
     * Flexible Search, order the query by Creation Time and the number of items per page as 1, we can configure the
     * Search Result to be an easy to find single result.
     *
     * @param productCode   The target product code
     * @param nth           The position of the review chronologically
     * @return The search result for a single {@Link CustomerReviewModel}
     */
    @Override
    public SearchPageData<CustomerReviewModel> findNthProductReview(String productCode, int nth) {
        FlexibleSearchQuery fquery = new FlexibleSearchQuery(FIND_NTH_PRODUCT_REVIEW);
        fquery.addQueryParameter("productCode", productCode);

        PaginationData pagination = new PaginationData();
        pagination.setPageSize(PAGE_SIZE);
        pagination.setNeedsTotal(true);
        pagination.setCurrentPage(nth);

        SearchPageData searchPage = new SearchPageData();
        searchPage.setPagination(pagination);

        PaginatedFlexibleSearchParameter param = new PaginatedFlexibleSearchParameter();
        param.setFlexibleSearchQuery(fquery);
        param.setSearchPageData(searchPage);

        return getPaginatedFlexibleSearchService().search(param);
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

    public PaginatedFlexibleSearchService getPaginatedFlexibleSearchService() {
        return paginatedFlexibleSearchService;
    }

    public void setPaginatedFlexibleSearchService(PaginatedFlexibleSearchService paginatedFlexibleSearchService) {
        this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
