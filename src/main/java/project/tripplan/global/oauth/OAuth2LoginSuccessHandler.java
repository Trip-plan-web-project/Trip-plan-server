package project.tripplan.global.oauth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.jwt.JWTService;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final UserRepositoryCustom userRepositoryCustom;
	private final JWTService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		log.info("OAuth2 Login 성공!");
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			String provider = oauthToken.getAuthorizedClientRegistrationId().toUpperCase(); // provider 정보 (예: kakao, google, naver)
			loginSuccess(request, response, oAuth2User, provider); // 로그인에 성공한 경우 access, refresh 토큰 생성
		} catch (Exception e) {
			throw e;
		}
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, String provider) throws IOException {
		String redirectUrl = String.format("http://localhost:3000/oauth/%s?socialId=%s", provider, oAuth2User.getSocialId());
		response.sendRedirect(redirectUrl);

		log.info("socialId를 포함하여 홈페이지로 리다이렉트: {}", redirectUrl);
	}

	private void loginSuccess(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User oAuth2User, String provider) throws IOException {
		// 요청의 Origin(출처) 가져오기
		String origin = request.getHeader("Origin");

		// 기본값 설정 (로컬 환경)
		String frontEndUrl = "http://localhost:3000";

		// 배포 환경일 경우 배포된 프론트엔드 URL로 변경
		if (origin != null && origin.contains("https://trip-plan-frontend.vercel.app")) {
			frontEndUrl = "https://trip-plan-frontend.vercel.app";
		}

		// 리다이렉트 URL 생성
		String redirectUrl = String.format("%s/oauth/%s?socialId=%s", frontEndUrl, provider, oAuth2User.getSocialId());

		// 클라이언트로 리다이렉트
		response.sendRedirect(redirectUrl);

		log.info("OAuth2 로그인 성공! 리다이렉트 URL: {}", redirectUrl);
	}
}
