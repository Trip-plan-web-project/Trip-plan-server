package project.tripplan.domain.admin.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.AddPointListReq;
import project.tripplan.domain.admin.service.AdminService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/admin/reports")
	public BaseResponse<?> getReportedHistory(@AuthenticationPrincipal User user,
		@RequestParam Integer category,
		@RequestParam(required = false) Long reasonId,
		@RequestParam(required = false) String startDate,
		@RequestParam(required = false) String endDate,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REPORTED_HISTORY_SUCCESS,
			adminService.getReportedHistory(category, reasonId, startDate, endDate, page, size));
	}

	 @GetMapping("/admin/points")
	public BaseResponse<?> getPointHistory(@AuthenticationPrincipal User user,
		 @RequestParam Integer category,
		 @RequestParam(required = false) String startDate,
		 @RequestParam(required = false) String endDate,
		 @RequestParam(defaultValue = "0") int page,
		 @RequestParam(defaultValue = "10") int size) {
		 return new BaseResponse<>(BaseResponseCode.GET_POINT_HISTORY_SUCCESS,
			 adminService.getPointHistory(category, startDate, endDate, page, size));
	 }

	@PostMapping("/admin/points")
	public BaseResponse<Void> addPoint(@AuthenticationPrincipal User user,
		@RequestBody AddPointListReq pointIds) {
		adminService.addPoint(pointIds);
		return new BaseResponse<>(BaseResponseCode.ADD_POINT_SUCCESS);
	}
}
