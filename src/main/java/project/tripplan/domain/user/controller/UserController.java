package project.tripplan.domain.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserCommentsRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.dto.UserProfileReq;
import project.tripplan.domain.user.dto.UserProfileRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.service.UserService;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	@Value("${cloud.prefix}")
	private String prefix;

	private final UserService userService;

	@GetMapping("/users/profile")
	public BaseResponse<UserProfileRes> getUserProFile(@AuthenticationPrincipal User user) {

		return new BaseResponse<>(
			BaseResponseCode.USER_GET_SUCCESS,
			UserProfileRes.builder()
				.userId(user.getId())
				.nickname(user.getNickname())
				.image(prefix + "/" + user.getImage())
				.build()
		);

	}

	@PatchMapping("/users/profile")
	public BaseResponse<Void> updateUserProfile(
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart("profile") String profile,
		@AuthenticationPrincipal User user
	) {
		ObjectMapper objectMapper = new ObjectMapper();
		UserProfileReq req = null;
		try {
			// Parse JSON string to Profile object
			req = objectMapper.readValue(profile, UserProfileReq.class);
		} catch (Exception e) {
			throw new CustomException(BaseResponseCode.JSON_PARSING_ERROR);
		}
		userService.updateUserProfile(user.getId(), req, image);
		return new BaseResponse<>(BaseResponseCode.USER_UPDATE_SUCCESS);
	}

	@GetMapping("/users/myplan")
	public BaseResponse<Page<UserPlanRes>> getUserPlans(
		@RequestParam int page,
		@RequestParam int size,
		@AuthenticationPrincipal User user
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		return new BaseResponse<>(BaseResponseCode.USER_PLAN_GET_SUCCESS,
			userService.getUserPlans(user.getId(), pageable)
		);
	}

	@GetMapping("/users/bookmarks")
	public BaseResponse<Page<UserBookmarkRes>> getBookmarks(
		@RequestParam int page,
		@RequestParam int size,
		@AuthenticationPrincipal User user
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		return new BaseResponse<>(BaseResponseCode.USER_BOOKMARK_GET_SUCCESS,
			userService.getUserBookmarks(user.getId(), pageable)
		);
	}

	@GetMapping("/users/comments")
	public BaseResponse<UserCommentsRes> getUserComments(
		@RequestParam(required = false) Long lastCommentId,
		@RequestParam int size,
		@AuthenticationPrincipal User user
	) {
		if (lastCommentId == 0) {
			lastCommentId = null;
		}

		return new BaseResponse<>(BaseResponseCode.USER_COMMENTS_GET_SUCCESS,
			userService.getUserCommentsNoOffset(user.getId(), lastCommentId, size)
		);
	}
}

