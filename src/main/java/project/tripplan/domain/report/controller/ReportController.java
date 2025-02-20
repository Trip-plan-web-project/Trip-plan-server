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

	@PostMapping("/reports/comments/{commentId}")
	public BaseResponse<Void> reportComment(@AuthenticationPrincipal User user, @PathVariable Long commentId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportComment(user, commentId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_COMMENT_SUCCESS);
	}

	@PostMapping("/reports/plans/{planId}")
	public BaseResponse<Void> reportPlan(@AuthenticationPrincipal User user, @PathVariable Long planId,
		@RequestBody ReportReasonDto reportReasonDto) {
		reportService.reportPlan(user, planId, reportReasonDto);
		return new BaseResponse<>(BaseResponseCode.REPORT_PLAN_SUCCESS);
	}
}
