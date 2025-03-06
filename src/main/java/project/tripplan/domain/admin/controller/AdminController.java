package project.tripplan.domain.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;
import project.tripplan.domain.admin.dto.ReportedPlanListRes;
import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;
import project.tripplan.domain.admin.dto.ReportedReviewRes;
import project.tripplan.domain.admin.service.AdminService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/admin/reports/plans")
	public BaseResponse<Page<ReportedPlanListRes>> getReportedPlanList(@AuthenticationPrincipal User user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REPORTED_PLAN_LIST_SUCCESS,
			adminService.getReportedPlanList(page, size));
	}

	@GetMapping("/admin/reports/plans/comments")
	public BaseResponse<Page<ReportedPlanCommentsRes>> getReportedPlanCommentList(@AuthenticationPrincipal User user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REPORTED_PLAN_COMMENT_LIST_SUCCESS,
			adminService.getReportedPlanCommentList(page, size));
	}

	@GetMapping("/admin/reports/reviews")
	public BaseResponse<Page<ReportedReviewRes>> getReportedReviewList(@AuthenticationPrincipal User user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REPORTED_REVIEW_LIST_SUCCESS,
			adminService.getReportedReviewList(page, size));
	}

	@GetMapping("/admin/reports/reviews/comments")
	public BaseResponse<Page<ReportedReviewCommentsRes>> getReportedReviewCommentList(
		@AuthenticationPrincipal User user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REPORTED_REVIEW_COMMENT_LIST_SUCCESS,
			adminService.getReportedReviewCommentList(page, size));
	}

	@GetMapping("/admin/reports/search")
	public BaseResponse<?> getReportedSearchList(@AuthenticationPrincipal User user,
		@RequestParam Integer category,
		@RequestParam(required = false) Long reasonId,
		@RequestParam(required = false) String startDate,
		@RequestParam(required = false) String endDate,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.SEARCH_REPORTED_LIST_SUCCESS,
			adminService.getReportedSearchList(category, reasonId, startDate, endDate, page, size));
	}
}
