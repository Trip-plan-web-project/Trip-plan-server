package project.tripplan.domain.report.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.report.dto.ReportReasonDto;
import project.tripplan.domain.report.service.ReportService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/reports/plans/comments/{commentId}")
	public BaseResponse<Void> reportPlanComment(@AuthenticationPrincipal User user, @PathVariable Long commentId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportPlanComment(user, commentId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_PLAN_COMMENT_SUCCESS);
	}

	@PostMapping("/reports/plans/{planId}")
	public BaseResponse<Void> reportPlan(@AuthenticationPrincipal User user, @PathVariable Long planId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportPlan(user, planId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_PLAN_SUCCESS);
	}

	@PostMapping("/reports/reviews/{reviewId}")
	public BaseResponse<Void> reportReview(@AuthenticationPrincipal User user, @PathVariable Long reviewId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportReview(user, reviewId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_REVIEW_SUCCESS);
	}

	@PostMapping("/reports/reviews/comments/{commentId}")
	public BaseResponse<Void> reportReviewComment(@AuthenticationPrincipal User user, @PathVariable Long commentId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportReviewComment(user, commentId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_REVIEW_COMMENT_SUCCESS);
	}
}
