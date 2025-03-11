package project.tripplan.domain.bookmark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.dto.AddReviewBookmarkRes;
import project.tripplan.domain.bookmark.service.ReviewBookmarkService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class ReviewBookmarkController {

	private final ReviewBookmarkService reviewBookmarkService;

	@PostMapping("/review/{reviewId}/bookmarks")
	public BaseResponse<AddReviewBookmarkRes> addBookmark(@AuthenticationPrincipal User user,
		@PathVariable Long reviewId) {
		return new BaseResponse<>(BaseResponseCode.ADD_REVIEW_BOOKMARK_SUCCESS,
			new AddReviewBookmarkRes(reviewBookmarkService.addReviewBookmark(user, reviewId)));
	}

	@DeleteMapping("/reivew/bookmarks/{reviewBookmarkId}")
	public BaseResponse<Void> deleteBookmark(@AuthenticationPrincipal User user, @PathVariable Long reviewBookmarkId) {
		reviewBookmarkService.deleteReviewBookmark(user, reviewBookmarkId);
		return new BaseResponse<>(BaseResponseCode.DELETE_REVIEW_BOOKMARK_SUCCESS);
	}

}
