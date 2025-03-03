package project.tripplan.domain.report.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.PlanComment;
import project.tripplan.domain.comment.entity.ReviewComment;
import project.tripplan.domain.comment.repository.PlanCommentRepository;
import project.tripplan.domain.comment.repository.ReviewCommentRepository;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.report.dto.ReportReasonDto;
import project.tripplan.domain.report.entity.PlanCommentReport;
import project.tripplan.domain.report.entity.PlanCommentReportReason;
import project.tripplan.domain.report.entity.PlanReport;
import project.tripplan.domain.report.entity.PlanReportReason;
import project.tripplan.domain.report.entity.ReportReason;
import project.tripplan.domain.report.entity.ReviewCommentReport;
import project.tripplan.domain.report.entity.ReviewCommentReportReason;
import project.tripplan.domain.report.entity.ReviewReport;
import project.tripplan.domain.report.entity.ReviewReportReason;
import project.tripplan.domain.report.repository.planReportReasonRepo.PlanReportReasonRepository;
import project.tripplan.domain.report.repository.planCommentReportReasonRepo.PlanCommentReportReasonRepository;
import project.tripplan.domain.report.repository.planCommentReportRepo.PlanCommentReportRepository;
import project.tripplan.domain.report.repository.planCommentReportRepo.PlanCommentReportRepositoryCustom;
import project.tripplan.domain.report.repository.planReportRepo.PlanReportRepository;
import project.tripplan.domain.report.repository.planReportRepo.PlanReportRepositoryCustom;
import project.tripplan.domain.report.repository.reportReasonRepo.ReportReasonRepositoryCustom;
import project.tripplan.domain.report.repository.reviewCommentReportReasonRepo.ReviewCommentReportReasonRepository;
import project.tripplan.domain.report.repository.reviewCommentReportRepo.ReviewCommentReportRepository;
import project.tripplan.domain.report.repository.reviewCommentReportRepo.ReviewCommentReportRepositoryCustom;
import project.tripplan.domain.report.repository.reviewReportReasonRepo.ReviewReportReasonRepository;
import project.tripplan.domain.report.repository.reviewReportRepo.ReviewReportRepository;
import project.tripplan.domain.report.repository.reviewReportRepo.ReviewReportRepositoryCustom;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final PlanCommentReportRepositoryCustom planCommentReportRepositoryCustom;
	private final PlanCommentReportRepository planCommentReportRepository;
	private final PlanCommentRepository planCommentRepository;
	private final ReportReasonRepositoryCustom reportReasonRepositoryCustom;
	private final PlanCommentReportReasonRepository planCommentReportReasonRepository;
	private final PlanReportRepositoryCustom planReportRepositoryCustom;
	private final PlanRepository planRepository;
	private final PlanReportRepository planReportRepository;
	private final PlanReportReasonRepository planReportReasonRepository;
	private final ReviewReportRepositoryCustom reviewReportRepositoryCustom;
	private final ReviewReportRepository reviewReportRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewCommentRepository reviewCommentRepository;
	private final ReviewReportReasonRepository reviewReportReasonRepository;
	private final ReviewCommentReportRepositoryCustom reviewCommentReportRepositoryCustom;
	private final ReviewCommentReportRepository reviewCommentReportRepository;
	private final ReviewCommentReportReasonRepository reviewCommentReportReasonRepository;

	@Transactional
	public void reportPlanComment(User user, Long commentId, ReportReasonDto reportReasonDto) {
		Optional<PlanCommentReport> findCommentReport = planCommentReportRepositoryCustom.findByUserIdAndCommentId(
			user.getId(), commentId);

		if (findCommentReport.isPresent()) {
			// 이미 신고했던 적이 있는 댓글인 경우
			throw new CustomException(BaseResponseCode.ALREADY_REPORTED_PLAN_COMMENT);
		} else {
			// 처음 신고한 댓글인 경우
			PlanComment findPlanComment = planCommentRepository.findById(commentId)
				.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

			PlanCommentReport planCommentReport = PlanCommentReport.builder()
				.user(user)
				.planComment(findPlanComment)
				.build();

			planCommentReportRepository.save(planCommentReport);

			List<ReportReason> findReasonList = reportReasonRepositoryCustom.findAllByIds(reportReasonDto.getReportReasons());

			List<PlanCommentReportReason> planCommentReportReasonList = findReasonList.stream()
				.map(reportReason -> PlanCommentReportReason.builder()
					.planCommentReport(planCommentReport)
					.reportReason(reportReason)
					.build())
				.toList();

			planCommentReportReasonRepository.saveAll(planCommentReportReasonList);
		}
	}

	@Transactional
	public void reportPlan(User user, Long planId, ReportReasonDto reportReasonDto) {
		Optional<PlanReport> findPlanReport = planReportRepositoryCustom.findByUserIdAndPlanId(user.getId(), planId);

		if (findPlanReport.isPresent()) {
			// 이미 신고했던 게시글인 경우
			throw new CustomException(BaseResponseCode.ALREADY_REPORTED_PLAN);
		} else {
			// 처음 신고한 게시글인 경우
			Plan findPlan = planRepository.findById(planId)
				.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

			PlanReport planReport = PlanReport.builder()
				.user(user)
				.plan(findPlan)
				.build();

			planReportRepository.save(planReport);

			List<ReportReason> findReasonList = reportReasonRepositoryCustom.findAllByIds(reportReasonDto.getReportReasons());

			List<PlanReportReason> planReportReasonList = findReasonList.stream()
				.map(reportReason -> PlanReportReason.builder()
					.planReport(planReport)
					.reportReason(reportReason)
					.build())
				.toList();

			planReportReasonRepository.saveAll(planReportReasonList);
		}
	}

	@Transactional
	public void reportReview(User user, Long reviewId, ReportReasonDto reportReasonDto) {
		Optional<ReviewReport> findReviewReport = reviewReportRepositoryCustom.findByUserIdAndReviewId(user.getId(),
			reviewId);

		if (findReviewReport.isPresent()) {
			//이미 신고받은 게시글인 경우
			throw new CustomException(BaseResponseCode.ALREADY_REPORTED_REVIEW);
		} else {
			// 처음 신고한 리뷰글인 경우
			Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_NOT_EXIST));

			ReviewReport reviewReport = ReviewReport.builder()
				.user(user)
				.review(findReview)
				.build();

			reviewReportRepository.save(reviewReport);

			List<ReportReason> findReasonList = reportReasonRepositoryCustom.findAllByIds(reportReasonDto.getReportReasons());

			List<ReviewReportReason> reviewReportReasonList = findReasonList.stream()
				.map(reportReason -> ReviewReportReason.builder()
					.reviewReport(reviewReport)
					.reportReason(reportReason)
					.build())
				.toList();

			reviewReportReasonRepository.saveAll(reviewReportReasonList);
		}
	}

	@Transactional
	public void reportReviewComment(User user, Long commentId, ReportReasonDto reportReasonDto) {
		Optional<ReviewCommentReport> findReviewCommentReport = reviewCommentReportRepositoryCustom.findByUserIdAndReportId(
			user.getId(), commentId);

		if (findReviewCommentReport.isPresent()) {
			// 이미 신고한적 있는 댓글
			throw new CustomException(BaseResponseCode.ALREADY_REPORTED_REVIEW_COMMENT);
		} else {
			ReviewComment findReviewComment = reviewCommentRepository.findById(commentId)
				.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_COMMENT_NOT_EXIST));

			ReviewCommentReport reviewCommentReport = ReviewCommentReport.builder()
				.reviewComment(findReviewComment)
				.user(user)
				.build();

			reviewCommentReportRepository.save(reviewCommentReport);

			List<ReportReason> findReasonList = reportReasonRepositoryCustom.findAllByIds(reportReasonDto.getReportReasons());

			List<ReviewCommentReportReason> findReviewCommentReportReasonList = findReasonList.stream()
				.map(reportReason -> ReviewCommentReportReason.builder()
					.reportReason(reportReason)
					.reviewCommentReport(reviewCommentReport)
					.build()
				)
				.toList();

			reviewCommentReportReasonRepository.saveAll(findReviewCommentReportReasonList);
		}
	}
}
