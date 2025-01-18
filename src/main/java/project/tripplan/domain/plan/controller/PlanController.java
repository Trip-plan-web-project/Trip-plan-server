package project.tripplan.domain.plan.controller;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.plan.dto.PlanConditionReq;
import project.tripplan.domain.plan.dto.PlanDetailRes;
import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.dto.PlanNoOffsetRes;
import project.tripplan.domain.plan.dto.PlanReq;
import project.tripplan.domain.plan.dto.PlanStatusReq;
import project.tripplan.domain.plan.service.PlanService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

	private final PlanService planService;

	@PostMapping("/plans")
	public BaseResponse<?> createPlan(
		@RequestPart("thumbnail") MultipartFile thumbnail,
		@Valid @RequestPart("plan") PlanReq planDto,
		@AuthenticationPrincipal User user
	) throws IOException {
		return new BaseResponse<>(BaseResponseCode.ADD_PLAN_SUCCESS, planService.savePlan(user, planDto, thumbnail));
	}

	@PatchMapping("/plans/{planId}/status")
	public BaseResponse<Void> updatePlanStatus(@AuthenticationPrincipal User user, @PathVariable Long planId,
		@Valid @RequestBody
		PlanStatusReq planStatusReq) {
		planService.updatePlanStatus(user, planId, planStatusReq);
		return new BaseResponse<>(BaseResponseCode.UPDATE_PLAN_STATUS_SUCCESS);
	}

	@GetMapping("/plans/{planId}")
	public BaseResponse<PlanDetailRes> getPlanInfoDetails(@AuthenticationPrincipal User user,
		@PathVariable Long planId) {
		return new BaseResponse<>(BaseResponseCode.GET_PLAN_DETAIL_INFO_SUCCESS,
			planService.getPlanInfoDetails(user, planId));
	}

	@PostMapping("/plans/no-offset")
	public PlanNoOffsetRes getPlans(@RequestBody PlanNoOffsetReq req) {
		return planService.getPlanNoOffset(req);
	}

	@GetMapping("/plans/search")
	public BaseResponse<PlanNoOffsetRes> getPlans(@ModelAttribute PlanConditionReq queryParam) {

		PlanNoOffsetReq req = new PlanNoOffsetReq();
		req.setSize(queryParam.getSize() != null ? queryParam.getSize() : 10);
		req.setSortBy(queryParam.getSortBy());
		req.setDirection(queryParam.getDirection());
		req.setLastValue(queryParam.getLastValue());
		req.setLastId(queryParam.getLastId());
		req.setDay(queryParam.getDay());
		req.setTransportCategoryName(queryParam.getTransportCategoryName());
		req.setPeople(queryParam.getPeople());
		req.setCategoryNames(queryParam.toCategoryReqList());

		return new BaseResponse<>(BaseResponseCode.GET_PLAN_DETAIL_INFO_SUCCESS, planService.getPlanNoOffset(req));
	}

}
