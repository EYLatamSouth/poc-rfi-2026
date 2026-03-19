package br.com.poccore.dao.impl;

import br.com.poccore.dao.PocProductDao;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class DefaultPocProductDao implements PocProductDao {

    private FlexibleSearchService flexibleSearchService;

    private static final String GET_PRODUCT_REVIEW_RATINGS = """
        SELECT {cr.rating}
        FROM {
            CustomerReview as cr
            JOIN Product as pr on {cr.product} = {pr.pk}
        }
        WHERE {pr.code} = ?code
        AND {cr.rating} >= 1 AND {cr.rating} <= 5
    """;
    private static final String GET_VERIFIED_REVIEW_RATING_COUNT = """
        SELECT COUNT(*)
        FROM {
            CustomerReview as cr
            JOIN Product as pr on {cr.product} = {pr.pk}
        }
        WHERE {pr.code} = ?code
        AND {cr.hasBoughtProduct} IN (?hasBoughtValues)
    """;
    private static final String GET_REVIEWS_WITH_USEFUL_RATING = """
        SELECT {cr.headline}, count({crr.isUseful})
        FROM {
            CustomerReviewRating as crr
            JOIN CustomerReview as cr on {cr.pk} = {crr.customerReview}
            JOIN Product as pr on {cr.product} = {pr.pk}
        }
        WHERE {pr.code} = ?code and {crr.isUseful} = 1 and {cr.hasBoughtProduct} = 1
        GROUP BY {cr.headline}
    """;
    private static final String GET_REVIEWS_BY_APPROVAL_STATUS = """
        SELECT COUNT(DISTINCT {cr.pk})
        FROM {
            CustomerReview as cr
            JOIN Product as pr on {cr.product} = {pr.pk}
            JOIN CustomerReviewApprovalType as cias on {cias.pk} = {cr.approvalStatus}
        }
        WHERE {pr.code} = ?code
        AND {cias.code} IN (?approvalCodes)
    """;
    private static final String GET_APPROVED_QUESTIONS_BY_CODE = """
        SELECT COUNT({cpi.pk})
        FROM {
            CustomerProductInquiry as cpi
            JOIN Product as pr on {cpi.product} = {pr.pk}
            JOIN CustomerInquiryApprovalStatus as cias on {cias.pk} = {cpi.approvalStatus}
        }
        WHERE {pr.code} = ?code and {cias.code} = 'APPROVED'
    """;
    private static final String GET_QUESTIONS_BY_APPROVAL_STATUS = """
        SELECT COUNT(DISTINCT {cpi.pk})
        FROM {
            CustomerProductInquiry as cpi
            JOIN Product as pr on {cpi.product} = {pr.pk}
            JOIN CustomerInquiryApprovalStatus as cias on {cias.pk} = {cpi.approvalStatus}
        }
        WHERE {pr.code} = ?code
        AND {cias.code} IN (?approvalCodes)
    """;
    private static final String GET_QUESTION_RESPONSE_TIMES = """
        SELECT {cpi.creationTime}, {cpi.answerDate}
        FROM {
            CustomerProductInquiry as cpi
            JOIN Product as pr on {cpi.product} = {pr.pk}
        }
        WHERE {pr.code} = ?code
        AND {cpi.answerDate} IS NOT NULL
    """;

    /**
     * Searches for the ratings of the Reviews given by the Customers for a given Product.
     *
     * @param productCode  The target product code.
     * @return {@link List<Double>} from given information.
     */
    @Override
    public List<Double> getProductRatings(String productCode) {
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_PRODUCT_REVIEW_RATINGS);
        fQuery.addQueryParameter("code", productCode);
        fQuery.setResultClassList(List.of(Double.class));

        SearchResult<Number> result = getFlexibleSearchService().search(fQuery);
        return result.getResult().stream().map(Number::doubleValue).toList();
    }

    /**
     * Searches for the count of Reviews of a given Product with different "hasBoughtProduct" values.
     *
     * @param productCode     The target product code.
     * @param hasBoughtValues Desired values of hasBoughtProduct to be considered in the result.
     * @return int from given information.
     */
    @Override
    public int getVerifiedReviewRatingCount(String productCode, List<Boolean> hasBoughtValues) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_VERIFIED_REVIEW_RATING_COUNT);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        query.addQueryParameter("hasBoughtValues", formatBooleansForQuery(hasBoughtValues));

        SearchResult<Integer> result = getFlexibleSearchService().search(query);
        return result.getResult().getFirst();

    }

    /**
     * Searches for a list of Review headlines and their amount of upvotes.
     * Only Reviews with at least one upvote are considered.
     *
     * @param productCode  The target product code.
     * @return {@link Map} from given information.
     */
    @Override
    public Map<String, Integer> getRatingsWithUpvoteCount(String productCode) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_REVIEWS_WITH_USEFUL_RATING);
        query.setResultClassList(Arrays.asList(String.class, Integer.class));
        query.addQueryParameter("code", productCode);

        SearchResult<List<Object>> result = getFlexibleSearchService().search(query);

        return result.getResult().stream()
            .filter(row -> (Integer) row.get(1) > 0)
            .collect(Collectors.toMap(
                row -> (String) row.getFirst(),
                row -> (Integer) row.get(1),
                (v1, v2) -> v1)
            );
    }

    /**
     * Searches for the count of Reviews of a given Product with different "approvalStatus" values.
     *
     * @param productCode      The target product code.
     * @param approvalStatuses Desired values of approvalStatus to be considered in the result.
     * @return int from given information.
     */
    @Override
    public int getReviewCountByStatus(String productCode, List<CustomerReviewApprovalType> approvalStatuses) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_REVIEWS_BY_APPROVAL_STATUS);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        query.addQueryParameter(
            "approvalCodes",
            approvalStatuses.stream().map(CustomerReviewApprovalType::getCode).toList()
        );
        SearchResult<Integer> result = getFlexibleSearchService().search(query);
        return result.getResult().getFirst();
    }

    /**
     * Searches for the count of Customer Inquiries of a given Product with "APPROVED" approvalStatus.
     *
     * @param productCode     The target product code.
     * @return int from given information.
     */
    @Override
    public int getTotalApprovedQuestions(String productCode) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_APPROVED_QUESTIONS_BY_CODE);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        SearchResult<Integer> result = getFlexibleSearchService().search(query);
        return result.getResult().getFirst();
    }

    /**
     * Searches for the count of Customer Inquiries of a given Product with different "approvalStatus" values.
     *
     * @param productCode      The target product code.
     * @param approvalStatuses Desired values of approvalStatus to be considered in the result.
     * @return int from given information.
     */
    @Override
    public int getQuestionCountByStatus(String productCode, List<CustomerInquiryApprovalStatus> approvalStatuses) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_QUESTIONS_BY_APPROVAL_STATUS);
        query.setResultClassList(Collections.singletonList(Integer.class));
        query.addQueryParameter("code", productCode);
        query.addQueryParameter(
            "approvalCodes",
            approvalStatuses.stream().map(CustomerInquiryApprovalStatus::getCode).toList()
        );
        SearchResult<Integer> result = getFlexibleSearchService().search(query);
        return result.getResult().getFirst();
    }

    /**
     * Searches for the dates of Customer Inquiry creation and response
     * and returns a list of those time periods.
     *
     * @param productCode     The target product code.
     * @return {@link List<Duration>} from given information.
     */
    @Override
    public List<Duration> getResponseTime(String productCode) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(GET_QUESTION_RESPONSE_TIMES);
        query.setResultClassList(Arrays.asList(Date.class, Date.class));
        query.addQueryParameter("code", productCode);
        final SearchResult<List<Object>> result = getFlexibleSearchService().search(query);

        return result.getResult().stream()
            .map(c -> Duration.between(((Date) c.getFirst()).toInstant(), ((Date) c.get(1)).toInstant()))
            .toList();
    }

    @Nonnull
    private static List<Integer> formatBooleansForQuery(List<Boolean> booleanValues) {
        return booleanValues.stream()
            .map(b -> BooleanUtils.isTrue(b) ? 1 : 0)
            .toList();
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
