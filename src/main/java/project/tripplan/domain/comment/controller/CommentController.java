package project.tripplan.domain.comment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.dto.CommentReq;
import project.tripplan.domain.comment.service.CommentService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/plans/{planId}/comments")
	public BaseResponse<Long> addComment(@AuthenticationPrincipal User user, @PathVariable Long planId,
		@Valid @RequestBody
		CommentReq commentReq) {
		return new BaseResponse<>(BaseResponseCode.ADD_COMMENT_SUCCESS,
			commentService.addComment(user, planId, commentReq));
	}

	@DeleteMapping("/plans/comments/{commentId}")
	public BaseResponse<Void> deleteComment(@AuthenticationPrincipal User user, @PathVariable Long commentId) {
		commentService.deleteComment(user, commentId);
		return new BaseResponse<>(BaseResponseCode.DELETE_COMMENT_SUCCESS);
	}

	@PatchMapping("/plans/comments/{commentId}")
	public BaseResponse<Void> updateComment(@AuthenticationPrincipal User user, @PathVariable Long commentId,
		@Valid @RequestBody CommentReq commentReq) {
		commentService.updateComment(user, commentId, commentReq);
		return new BaseResponse<>(BaseResponseCode.UPDATE_COMMENT_SUCCESS);
	}
}
