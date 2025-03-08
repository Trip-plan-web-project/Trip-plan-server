package project.tripplan.domain.comment.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.dto.ReviewCommentReq;
import project.tripplan.domain.comment.service.ReviewCommentService;
import project.tripplan.domain.plan.dto.PlanCommentsRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class ReviewCommentController {

	private final ReviewCommentService reviewCommentService;

	@GetMapping("/reviews/{reviewId}/comments")
	public BaseResponse<Page<PlanCommentsRes>> getPlanComments(
		@AuthenticationPrincipal User user,
		@PathVariable Long reviewId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "4") int size) {
		return new BaseResponse<>(BaseResponseCode.GET_REVIEW_COMMENTS_LIST_SUCCESS,
			reviewCommentService.getReviewComments(reviewId, page, size));
	}

	@PostMapping("/reviews/{reviewId}/comments")
	public BaseResponse<Long> addComment(@AuthenticationPrincipal User user, @PathVariable Long reviewId,
		@Valid @RequestBody
		ReviewCommentReq reviewCommentReq) {
		return new BaseResponse<>(BaseResponseCode.ADD_COMMENT_SUCCESS,
			reviewCommentService.addComment(user, reviewId, reviewCommentReq));
	}

	@DeleteMapping("/reviews/comments/{commentId}")
	public BaseResponse<Void> deleteComment(@AuthenticationPrincipal User user, @PathVariable Long commentId) {
		reviewCommentService.deleteReviewComment(user, commentId);
		return new BaseResponse<>(BaseResponseCode.DELETE_COMMENT_SUCCESS);
	}

	@PatchMapping("/reviews/comments/{commentId}")
	public BaseResponse<Void> updateComment(@AuthenticationPrincipal User user, @PathVariable Long commentId,
		@Valid @RequestBody ReviewCommentReq reviewCommentReq) {
		reviewCommentService.updateReviewComment(user, commentId, reviewCommentReq);
		return new BaseResponse<>(BaseResponseCode.UPDATE_COMMENT_SUCCESS);
	}
}
