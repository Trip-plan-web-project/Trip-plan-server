package project.tripplan.domain.bookmark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.service.BookmarkService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/plans/{planId}/bookmarks")
	public BaseResponse<Long> addBookmark(@AuthenticationPrincipal User user, @PathVariable Long planId) {
		return new BaseResponse<>(BaseResponseCode.ADD_BOOKMARK_SUCCESS, bookmarkService.addBookmark(user, planId));
	}

	@DeleteMapping("/plans/bookmarks/{bookmarkId}")
	public BaseResponse<Void> deleteBookmark(@AuthenticationPrincipal User user, @PathVariable Long bookmarkId) {
		bookmarkService.deleteBookmark(user, bookmarkId);
		return new BaseResponse<>(BaseResponseCode.DELETE_BOOKMARK_SUCCESS);
	}
}
