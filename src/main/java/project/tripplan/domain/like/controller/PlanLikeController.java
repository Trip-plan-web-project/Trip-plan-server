package project.tripplan.domain.like.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.like.service.PlanLikeService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class PlanLikeController {

	private final PlanLikeService planLikeService;

	@PostMapping("/plans/{planId}/like")
	public BaseResponse<Long> addPlanLike(@AuthenticationPrincipal User user, @PathVariable Long planId) {
		return new BaseResponse<>(BaseResponseCode.ADD_PLAN_LIKE_SUCCESS, planLikeService.addPlanLike(user, planId));
	}

	@DeleteMapping("/plans/like/{planLikeId}")
	public BaseResponse<Void> deletePlanLike(@AuthenticationPrincipal User user, @PathVariable Long planLikeId) {
		planLikeService.deletePlanLike(user, planLikeId);
		return new BaseResponse<>(BaseResponseCode.DELETE_PLAN_LIKE_SUCCESS);
	}
}
