package project.tripplan.domain.plan.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.plan.dto.HomeRes;
import project.tripplan.domain.plan.dto.PlanCommentsRes;
import project.tripplan.domain.plan.dto.PlanConditionReq;
import project.tripplan.domain.plan.dto.PlanDetailRes;
import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.dto.PlanNoOffsetRes;
import project.tripplan.domain.plan.dto.PlanReq;
import project.tripplan.domain.plan.dto.PlanStatusReq;
import project.tripplan.domain.plan.dto.PlanUpdateReq;
import project.tripplan.domain.plan.service.PlanService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

	private final PlanService planService;
	private final UserRepository userRepository;

	@PostMapping(value = "/plans", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public BaseResponse<Void> savePlan(
		@RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
		@Valid @RequestPart("plan") PlanReq planDto,
		@AuthenticationPrincipal User user
	) throws IOException {
		planService.savePlan(user, planDto, thumbnail);
		return new BaseResponse<>(BaseResponseCode.ADD_PLAN_SUCCESS);
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

	@GetMapping("/plans/search")
	public BaseResponse<PlanNoOffsetRes> getPlans(@Valid @ModelAttribute PlanConditionReq queryParam) {

		PlanNoOffsetReq req = new PlanNoOffsetReq();

		if (queryParam.getLastValue().equals("null")) {
			req.setLastValue(null);
		} else {
			req.setLastValue(queryParam.getLastValue());
		}

		if (queryParam.getLastId() == 0) {
			req.setLastId(null);
		} else {
			req.setLastId(queryParam.getLastId());
		}

		req.setKeyword(queryParam.getKeyword());
		req.setSize(queryParam.getSize() != null ? queryParam.getSize() : 10);
		req.setSortBy(queryParam.getSortBy());
		req.setDirection(queryParam.getDirection());
		req.setDay(queryParam.getDay());
		req.setTransportCategoryName(queryParam.getTransportCategoryName());
		req.setPeople(queryParam.getPeople());
		req.setCategoryNames(queryParam.toCategoryReqList());

		return new BaseResponse<>(BaseResponseCode.GET_PLAN_SEARCH_CONDITION_SUCCESS, planService.getPlanNoOffset(req));
	}

	@GetMapping("/home")
	public BaseResponse<HomeRes> getHome() {
		return new BaseResponse<>(BaseResponseCode.GET_HOME_SUCCESS, planService.getHome());
	}

	@GetMapping("/plans/{planId}/comments")
	public BaseResponse<Page<PlanCommentsRes>> getPlanComments(
		@AuthenticationPrincipal User user,
		@PathVariable Long planId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "4") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_PLAN_COMMENTS_LIST_SUCCESS,
			planService.getPlanComments(planId, page, size));
	}

	@DeleteMapping("/plans/{planId}")
	public BaseResponse<Void> deletePlan(
		@AuthenticationPrincipal User user,
		@PathVariable Long planId) {
		planService.deletePlan(planId, user);
		return new BaseResponse<>(BaseResponseCode.DELETE_PLAN_SUCCESS);
	}

	@PostMapping("/plans/{planId}/copy")
	public BaseResponse<Void> copyPlan(
		@AuthenticationPrincipal User user,
		@PathVariable Long planId
	) {
		planService.copyPlan(planId, user.getId());
		return new BaseResponse<>(BaseResponseCode.COPY_PLAN_SUCCESS);
	}

	@PatchMapping(value = "/plans", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public BaseResponse<?> updatePlan(
		@RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
		@Valid @RequestPart("plan") PlanUpdateReq planDto,
		@AuthenticationPrincipal User user
	) throws IOException {
		return new BaseResponse<>(BaseResponseCode.UPDATE_PLAN_SUCCESS,
			planService.updatePlan(user, planDto, thumbnail));
	}
}
