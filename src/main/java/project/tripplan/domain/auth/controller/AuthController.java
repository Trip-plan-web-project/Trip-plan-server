package project.tripplan.domain.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.auth.dto.LoginRes;
import project.tripplan.domain.auth.service.AuthService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/token/issue/{socialId}")
	public BaseResponse<LoginRes> issueToken(@PathVariable String socialId) {
		return new BaseResponse<>(BaseResponseCode.LOGIN_SUCCESS, authService.issueAccessAndRefresh(socialId));
	}

	@PostMapping("/token/reissue/{socialId}")
	public BaseResponse<Void> reissueToken(@PathVariable String socialId,
		@RequestHeader("Authorization_refresh") String refreshToken,
		@RequestHeader("Authorization") String accessToken,
		HttpServletResponse response) {
		authService.reissueToken(socialId, accessToken, refreshToken, response);
		return new BaseResponse<>(BaseResponseCode.REISSUE_TOKEN_SUCCESS);
	}

	@PostMapping("/users/logout")
	public BaseResponse<Void> logout(HttpServletRequest request) {
		authService.logout(request);
		return new BaseResponse<>(BaseResponseCode.LOGOUT_SUCCESS);
	}
}
