package br.com.poccore.dao;

import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface PocProductDao {
    List<Double> getProductRatings(String productCode);

    int getVerifiedReviewRatingCount(String productCode, List<Boolean> hasBoughtValues);

    Map<String, Integer> getRatingsWithUpvoteCount(String productCode);

    int getReviewCountByStatus(String productCode, List<CustomerReviewApprovalType> approvalStatuses);

    int getTotalApprovedQuestions(String productCode);

    int getQuestionCountByStatus(String productCode, List<CustomerInquiryApprovalStatus> approvalStatuses);

    List<Duration> getResponseTime(String productCode);
}
