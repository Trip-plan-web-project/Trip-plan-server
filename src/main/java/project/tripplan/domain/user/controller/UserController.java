package project.tripplan.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public BaseResponse<Void> userProfileUpdate(
		@RequestPart("image") MultipartFile image,
		@RequestPart("profile") UserProfileReq req,
		@AuthenticationPrincipal User user
	) {
		userService.userProfileUpdate(user.getId(), req.getNickname(), image);
		return new BaseResponse<>(BaseResponseCode.USER_UPDATE_SUCCESS);
	}
}