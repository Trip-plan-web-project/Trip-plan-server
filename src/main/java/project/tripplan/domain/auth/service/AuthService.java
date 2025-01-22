package project.tripplan.domain.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.auth.dto.LoginRes;
import project.tripplan.domain.auth.refreshToken.entity.RefreshToken;
import project.tripplan.domain.auth.refreshToken.repository.RefreshTokenRepository;
import project.tripplan.domain.auth.refreshToken.repository.RefreshTokenRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;
import project.tripplan.global.jwt.JWTService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
	private final UserRepositoryCustom userRepositoryCustom;
	private final JWTService jwtService;
	private final RefreshTokenRepositoryCustom refreshTokenRepositoryCustom;

	@Override
	public UserDetails loadUserByUsername(String socialId) throws UsernameNotFoundException {
		User findUser = userRepositoryCustom.findBySocialId(socialId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

		return org.springframework.security.core.userdetails.User.builder()
			.username(findUser.getSocialId())
			.roles(findUser.getUserRole().name())
			.build();
	}

	@Transactional
	public LoginRes issueAccessAndRefresh(String socialId) {
		// access와 refresh 둘다 발급 해주는 경우
		User findUser = userRepositoryCustom.findBySocialId(socialId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

		String accessToken = jwtService.createAccessToken(socialId);
		String refreshToken = jwtService.createRefreshToken();

		// Refresh Token 저장
		jwtService.updateRefreshToken(findUser, socialId, refreshToken);

		return new LoginRes(findUser.getNickname(), accessToken, refreshToken);
	}

	@Transactional
	public void reissueToken(String socialId, String accessToken, String refresh, HttpServletResponse response) {
		User findUser = userRepositoryCustom.findBySocialId(socialId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

		String refreshToken = refresh.replace("Bearer", "").replace(" ", "");

		// Refresh Token을 DB에서 조회
		RefreshToken findRefreshToken = refreshTokenRepositoryCustom.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REFRESHTOKEN_NOT_EXIST));

		// Refresh Token 유효성 검사
		boolean isRefreshTokenValid = jwtService.isTokenValid(refreshToken);

		// Access Token 생성
		String reIssuedAccessToken = jwtService.createAccessToken(socialId);

		if (isRefreshTokenValid) {
			// Refresh Token이 유효한 경우 Access Token만 재발급
			jwtService.sendAccessToken(response, reIssuedAccessToken);
			log.info("Access Token만 재발급되었습니다.");
		} else {
			// Refresh Token이 만료된 경우 Access Token과 Refresh Token 모두 재발급
			String reIssuedRefreshToken = reIssueRefreshToken(findUser); // Refresh Token 재발급
			jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
			log.info("Access Token과 Refresh Token이 모두 재발급되었습니다.");
		}
	}

	private String reIssueRefreshToken(User user) {
		if (user == null) {
			// 로그아웃 처리
			log.info("로그아웃 완료");
			return "none";
		}

		String reIssuedRefreshToken = jwtService.createRefreshToken();
		RefreshToken findRefreshToken = refreshTokenRepositoryCustom.findByUserId(user.getId())
			.orElseThrow(() -> new CustomException(BaseResponseCode.REFRESHTOKEN_NOT_EXIST));
		findRefreshToken.updateRefreshToken(reIssuedRefreshToken);
		log.info("refreshToken 재발급 및 최신화");
		return reIssuedRefreshToken;
	}
}
