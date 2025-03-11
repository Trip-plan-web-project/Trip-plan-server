package project.tripplan.domain.like.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.like.service.ReviewLikeService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class ReviewLikeController {

	private final ReviewLikeService reviewLikeService;

	@PostMapping("/review/{reviewId}/like")
	public BaseResponse<Long> addReviewLike(@AuthenticationPrincipal User user, @PathVariable Long reviewId) {
		return new BaseResponse<>(BaseResponseCode.ADD_PLAN_LIKE_SUCCESS,
			reviewLikeService.addReviewLike(user, reviewId));
	}

	@DeleteMapping("/review/like/{reviewLikeId}")
	public BaseResponse<Void> deleteReviewLike(@AuthenticationPrincipal User user, @PathVariable Long reviewLikeId) {
		reviewLikeService.deleteReviewLike(user, reviewLikeId);
		return new BaseResponse<>(BaseResponseCode.DELETE_PLAN_LIKE_SUCCESS);
	}
}
