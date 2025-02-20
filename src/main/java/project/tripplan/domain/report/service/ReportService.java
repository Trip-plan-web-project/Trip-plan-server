package project.tripplan.domain.report.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepository;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.report.dto.ReportReasonDto;
import project.tripplan.domain.report.entity.CommentReport;
import project.tripplan.domain.report.entity.CommentReportReason;
import project.tripplan.domain.report.entity.PlanReport;
import project.tripplan.domain.report.entity.PlanReportReason;
import project.tripplan.domain.report.entity.ReportReason;
import project.tripplan.domain.report.repository.PlanReportReasonRepository;
import project.tripplan.domain.report.repository.commentReportReasonRepo.CommentReportReasonRepository;
import project.tripplan.domain.report.repository.commentReportRepo.CommentReportRepository;
import project.tripplan.domain.report.repository.commentReportRepo.CommentReportRepositoryCustom;
import project.tripplan.domain.report.repository.planReportRepo.PlanReportRepository;
import project.tripplan.domain.report.repository.planReportRepo.PlanReportRepositoryCustom;
import project.tripplan.domain.report.repository.reportReasonRepo.ReportReasonRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final CommentReportRepositoryCustom commentReportRepositoryCustom;
	private final CommentReportRepository commentReportRepository;
	private final CommentRepository commentRepository;
	private final ReportReasonRepositoryCustom reportReasonRepositoryCustom;
	private final CommentReportReasonRepository commentReportReasonRepository;
	private final PlanReportRepositoryCustom planReportRepositoryCustom;
	private final PlanRepository planRepository;
	private final PlanReportRepository planReportRepository;
	private final PlanReportReasonRepository planReportReasonRepository;

	@Transactional
	public void reportComment(User user, Long commentId, ReportReasonDto reportReasonDto) {
		Optional<CommentReport> findCommentReport = commentReportRepositoryCustom.findByUserIdAndCommentId(
			user.getId(), commentId);

		if (findCommentReport.isPresent()) {
			// 이미 신고했던 적이 있는 댓글인 경우
			throw new CustomException(BaseResponseCode.ALREADY_REPORTED_COMMENT);
		} else {
			// 처음 신고한 댓글인 경우
			Comment findComment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

			CommentReport commentReport = CommentReport.builder()
				.user(user)
				.comment(findComment)
				.build();

			commentReportRepository.save(commentReport);

			List<ReportReason> findReasonList = reportReasonRepositoryCustom.findAllByIds(reportReasonDto.getReportReasons());

			List<CommentReportReason> commentReportReasonList = findReasonList.stream()
				.map(reportReason -> CommentReportReason.builder()
					.commentReport(commentReport)
					.reportReason(reportReason)
					.build())
				.toList();

			commentReportReasonRepository.saveAll(commentReportReasonList);
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
}
