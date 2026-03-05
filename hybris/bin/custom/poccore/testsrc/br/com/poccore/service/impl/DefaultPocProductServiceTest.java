package br.com.poccore.service.impl;

import br.com.poccore.dao.PocProductDao;
import br.com.poccore.enums.CustomerInquiryApprovalStatus;
import br.com.vivo.facades.product.data.PocProductEngagementSummaryInfoData;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class DefaultPocProductServiceTest {
	@InjectMocks
	private DefaultPocProductService defaultPocProductService;

	@Mock
	private PocProductDao pocProductDao;

	private static final String PRODUCT_CODE = "testProduct";

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		defaultPocProductService.setPocProductDao(pocProductDao);

		when(pocProductDao.getRatingsWithUpvoteCount(PRODUCT_CODE))
			.thenReturn(new TreeMap<>(Map.of("5 stars", 50, "4 stars", 30)));
		when(pocProductDao.getTotalApprovedQuestions(PRODUCT_CODE)).thenReturn(5);
	}

	@Test
	public void testGetEngagementSummary_withValidDurations() {
		when(pocProductDao.getProductRatings(PRODUCT_CODE)).thenReturn(List.of(1.0, 2.0, 3.0, 2.0));
		when(pocProductDao.getVerifiedReviewRatingCount(PRODUCT_CODE, Collections.singletonList(true)))
			.thenReturn(15);
		when(pocProductDao.getVerifiedReviewRatingCount(PRODUCT_CODE, Arrays.asList(true, false)))
			.thenReturn(20);
		when(pocProductDao.getReviewCountByStatus(
			PRODUCT_CODE,
			Collections.singletonList(CustomerReviewApprovalType.APPROVED))
		).thenReturn(20);
		when(pocProductDao.getReviewCountByStatus(PRODUCT_CODE, Arrays.asList(CustomerReviewApprovalType.values())))
			.thenReturn(25);
		when(pocProductDao.getQuestionCountByStatus(
			PRODUCT_CODE,
			List.of(
				CustomerInquiryApprovalStatus.APPROVED,
				CustomerInquiryApprovalStatus.UNAPPROVED,
				CustomerInquiryApprovalStatus.PENDING
			)
		)).thenReturn(20);
		when(pocProductDao.getQuestionCountByStatus(
			PRODUCT_CODE,
			Collections.singletonList(CustomerInquiryApprovalStatus.APPROVED)
		)).thenReturn(10);
		when(pocProductDao.getResponseTime(PRODUCT_CODE)).thenReturn(List.of(Duration.ofHours(1), Duration.ofHours(2)));

		PocProductEngagementSummaryInfoData result = defaultPocProductService.getEngagementSummary(PRODUCT_CODE);

		assertNotNull(result);
		assertEquals("2.00", result.getRating());
		assertDistribution(result);
		assertEquals("75.00", result.getVerified());
		assertNotNull(result.getTopReviews());
		assertEquals(2, result.getTopReviews().size());
		assertEquals("5 stars", result.getTopReviews().getFirst().getHeadLine());
		assertEquals("80.00", result.getReviewPublication());
		assertEquals(5, result.getQuestionCount().intValue());
		assertEquals("50.00", result.getQuestionPublication());
		assertEquals(90, result.getResponseTime().intValue());
	}

	@Test
	public void testGetEngagementSummary_withNoQuestionsAndNoReviews() {
		when(pocProductDao.getProductRatings(PRODUCT_CODE)).thenReturn(Collections.emptyList());
		when(pocProductDao.getVerifiedReviewRatingCount(PRODUCT_CODE, Collections.singletonList(true)))
			.thenReturn(0);
		when(pocProductDao.getVerifiedReviewRatingCount(PRODUCT_CODE, Arrays.asList(true, false)))
			.thenReturn(0);
		when(pocProductDao.getReviewCountByStatus(
			PRODUCT_CODE,
			Collections.singletonList(CustomerReviewApprovalType.APPROVED))
		).thenReturn(0);
		when(pocProductDao.getReviewCountByStatus(PRODUCT_CODE, Arrays.asList(CustomerReviewApprovalType.values())))
			.thenReturn(0);
		when(pocProductDao.getQuestionCountByStatus(
			PRODUCT_CODE,
			List.of(
				CustomerInquiryApprovalStatus.APPROVED,
				CustomerInquiryApprovalStatus.UNAPPROVED,
				CustomerInquiryApprovalStatus.PENDING
			)
		)).thenReturn(0);
		when(pocProductDao.getQuestionCountByStatus(
			PRODUCT_CODE,
			Collections.singletonList(CustomerInquiryApprovalStatus.APPROVED)
		)).thenReturn(0);
		when(pocProductDao.getResponseTime(PRODUCT_CODE)).thenReturn(Collections.emptyList());

		PocProductEngagementSummaryInfoData result = defaultPocProductService.getEngagementSummary(PRODUCT_CODE);

		assertNotNull(result);
		assertEquals("0.00", result.getRating());
		assertEquals("0.00", result.getVerified());
		assertEquals("0.00", result.getReviewPublication());
		assertEquals("0.00", result.getQuestionPublication());
		assertEquals(0, result.getResponseTime().intValue());
	}

	private static void assertDistribution(PocProductEngagementSummaryInfoData result) {
		assertNotNull(result.getDistribution());
		assertEquals(5, result.getDistribution().size());
		assertEquals(1, result.getDistribution().getFirst().getGradeCount().intValue());
		assertEquals(2, result.getDistribution().get(1).getGradeCount().intValue());
		assertEquals(1, result.getDistribution().get(2).getGradeCount().intValue());
		assertEquals(0, result.getDistribution().get(3).getGradeCount().intValue());
		assertEquals(0, result.getDistribution().get(4).getGradeCount().intValue());
	}
}