<<<<<<< Updated upstream
// package project.tripplan.domain.user.controller;
//
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import project.tripplan.domain.plan.dto.PlanDetailRes;
// import project.tripplan.domain.user.entity.User;
// import project.tripplan.global.common.response.BaseResponse;
// import project.tripplan.global.common.response.BaseResponseCode;
//
// @Slf4j
// @RestController
// @RequiredArgsConstructor
// public class UserController {
//
// 	@GetMapping
// 	public BaseResponse<PlanDetailRes> getUserProFile(@AuthenticationPrincipal User user) {
// 		return new BaseResponse<>(BaseResponseCode.GET_PLAN_DETAIL_INFO_SUCCESS,
// 			planService.getPlanInfoDetails(user, planId));
// 	}
// }
=======
package project.tripplan.domain.user.controller;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.dto.UserProfileReq;
import project.tripplan.domain.user.dto.UserProfileRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.service.UserService;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/users/profile")
	public BaseResponse<UserProfileRes> getUserProFile(@AuthenticationPrincipal User user) {

		return new BaseResponse<>(
			BaseResponseCode.USER_GET_SUCCESS,
			UserProfileRes.builder()
				.userId(user.getId())
				.nickname(user.getNickname())
				.image(user.getImage())
				.email(user.getEmail())
				.build()
		);

	}

	@PatchMapping("/users/profile")
	public BaseResponse<Void> updateUserProfile(
		@RequestPart("image") MultipartFile image,
		@RequestPart("profile") UserProfileReq req,
		@AuthenticationPrincipal User user
	) {
		userService.updateUserProfile(user.getId(), req.getNickname(), image);
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
}

>>>>>>> Stashed changes
