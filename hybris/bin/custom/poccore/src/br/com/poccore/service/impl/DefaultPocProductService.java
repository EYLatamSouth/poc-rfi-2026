package br.com.poccore.service.impl;

import br.com.poc.occ.dto.product.PocProductReviewInfoData;
import br.com.poc.occ.dto.product.PocProductReviewsInfoData;
import br.com.poccore.dao.PocProductDao;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.poccore.service.PocProductService;
import br.com.pocfacades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import jakarta.annotation.Nonnull;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultPocProductService implements PocProductService {

    private PocProductDao pocProductDao;

    public PocProductEngagementSummaryInfoData getEngagementSummary(String productCode) {

        PocProductEngagementSummaryInfoData pocProductEngagementSummaryInfoData = new PocProductEngagementSummaryInfoData();

        List<Double> productRatings = getPocProductDao().getProductRatings(productCode);
        pocProductEngagementSummaryInfoData.setRating(getProductAverageRating(productRatings));
        pocProductEngagementSummaryInfoData.setDistribution(getProductRatingDistribution(productRatings));
        pocProductEngagementSummaryInfoData.setVerified(getReviewVerifiedPercentage(productCode));
        pocProductEngagementSummaryInfoData.setTopReviews(getReviewsByUsefulness(productCode));
        pocProductEngagementSummaryInfoData.setReviewPublication(getReviewApprovedPercentage(productCode));

        int totalQuestions  = getPocProductDao().getTotalApprovedQuestions(productCode);
        pocProductEngagementSummaryInfoData.setQuestionCount(totalQuestions);
        pocProductEngagementSummaryInfoData.setQuestionPublication(getQuestionApprovedPercentage(productCode));
        pocProductEngagementSummaryInfoData.setResponseTime(getAverageResponseTime(productCode));

        return  pocProductEngagementSummaryInfoData;
    }

    private String getProductAverageRating(List<Double> productRatings) {
        double rating = 0.0;
        if (!productRatings.isEmpty()) {
            double sum = productRatings.stream().mapToDouble(Double::doubleValue).sum();
            rating = (sum / productRatings.size());
        }

        return formatFloatingValue(rating);
    }

    private List<PocProductReviewInfoData> getProductRatingDistribution(List<Double> productRatings) {
        SortedMap<Integer, Integer> distribution = productRatings.stream()
            .map(Double::intValue)
            .collect(Collectors.groupingBy(
                rating -> rating,
                TreeMap::new,
                Collectors.summingInt(r -> 1)
            ));

        for (int i = 1; i <= 5; i++) {
            distribution.putIfAbsent(i, 0);
        }
        return distribution.entrySet()
            .stream()
            .map(entry -> {
                PocProductReviewInfoData data = new PocProductReviewInfoData();
                data.setGrade(entry.getKey());
                data.setGradeCount(entry.getValue());
                return data;
            })
            .toList();
    }

    private String getReviewVerifiedPercentage(String productCode) {
        double verified = 0.0;
        int reviewsFromCustomersThatBoughtTheProduct  =
            getPocProductDao().getVerifiedReviewRatingCount(productCode, Collections.singletonList(true));
        int totalProductReviews  =
            getPocProductDao().getVerifiedReviewRatingCount(productCode, Arrays.asList(true, false));
        if(totalProductReviews > 0) {
            verified = (((double) reviewsFromCustomersThatBoughtTheProduct / totalProductReviews) * 100);
        }

		return formatFloatingValue(verified);
    }

    @Nonnull
    private List<PocProductReviewsInfoData> getReviewsByUsefulness(String productCode) {
        Map<String, Integer> listNReviews = getPocProductDao().getRatingsWithUpvoteCount(productCode);
        List<PocProductReviewsInfoData> toplist = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listNReviews.entrySet()) {
            PocProductReviewsInfoData pocProductReviewsInfoData = new PocProductReviewsInfoData();
            pocProductReviewsInfoData.setHeadLine(entry.getKey());
            pocProductReviewsInfoData.setReviewCount(entry.getValue());
            toplist.add(pocProductReviewsInfoData);
        }
        return toplist.stream()
            .sorted(Comparator.comparingInt(PocProductReviewsInfoData::getReviewCount).reversed())
            .toList();
    }

    private String getReviewApprovedPercentage(String productCode) {
        double publication = 0.0;
        int totalReviews  =
            getPocProductDao().getReviewCountByStatus(productCode, Arrays.asList(CustomerReviewApprovalType.values()));
        int approvedReviews  = getPocProductDao().getReviewCountByStatus(
            productCode,
            Collections.singletonList(CustomerReviewApprovalType.APPROVED)
        );
        if(totalReviews > 0) {
            publication = (((double) approvedReviews / totalReviews) * 100);
        }

        return formatFloatingValue(publication);
    }
    private String getQuestionApprovedPercentage(String productCode) {
        double publication = 0.0;
        int totalReviews  = getPocProductDao().getQuestionCountByStatus(
            productCode,
            List.of(
                CustomerInquiryApprovalStatus.APPROVED,
                CustomerInquiryApprovalStatus.UNAPPROVED,
                CustomerInquiryApprovalStatus.PENDING
            )
        );
        int approvedReviews  = getPocProductDao().getQuestionCountByStatus(
            productCode,
            Collections.singletonList(CustomerInquiryApprovalStatus.APPROVED)
        );
        if(totalReviews > 0) {
            publication = (((double) approvedReviews / totalReviews) * 100);
        }

        return formatFloatingValue(publication);
    }

    private int getAverageResponseTime(String productCode) {
        List<Duration> durationsBetweenQuestionAndAnswer  = getPocProductDao().getResponseTime(productCode);
        double averageInMinutes = calculateAverageDurationInMinutes(durationsBetweenQuestionAndAnswer);

        return (int) averageInMinutes;
    }

    private static String formatFloatingValue(double rating) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(rating);
    }

    public static double calculateAverageDurationInMinutes(List<Duration> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            return 0.0;
        }
        long totalMinutes = 0;
        for (Duration duration : durations) {
            totalMinutes += duration.toMinutes();
        }

        return (double) totalMinutes / durations.size();
    }

    public PocProductDao getPocProductDao() {
        return pocProductDao;
    }

    public void setPocProductDao(PocProductDao pocProductDao) {
        this.pocProductDao = pocProductDao;
    }



}
