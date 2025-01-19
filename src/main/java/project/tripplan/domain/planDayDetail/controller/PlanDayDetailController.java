package project.tripplan.domain.planDayDetail.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.planDayDetail.dto.PlanDetailDayRes;
import project.tripplan.domain.planDayDetail.service.PlanDayDetailService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class PlanDayDetailController {

	private final PlanDayDetailService planDayDetailService;

	@GetMapping("/plans/{planId}/route")
	public BaseResponse<PlanDetailDayRes> getPlanDayDetail(@AuthenticationPrincipal User user,
		@RequestParam(value = "day", required = false) Integer day,
		@PathVariable Long planId) {
		return new BaseResponse<>(BaseResponseCode.GET_PLAN_DETAIL_DAY_SUCCESS,
			planDayDetailService.getPlanDayDetail(day, planId));
	}
}
