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
			loginSuccess(response, oAuth2User, provider); // 로그인에 성공한 경우 access, refresh 토큰 생성
		} catch (Exception e) {
			throw e;
		}
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, String provider) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getSocialId());
		String refreshToken = jwtService.createRefreshToken();

		jwtService.setAccessTokenHeader(response, accessToken);
		jwtService.setRefreshTokenHeader(response, refreshToken);

		// Refresh Token 저장
		jwtService.updateRefreshToken(oAuth2User.getSocialId(), refreshToken);

		response.sendRedirect("http://localhost:3000/oauth/" + provider);
		log.info("토큰을 헤더에 담아서 홈페이지로 리다이렉트");
	}
}
