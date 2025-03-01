package project.tripplan.global.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.auth.refreshToken.entity.BlackList;
import project.tripplan.domain.auth.refreshToken.entity.RefreshToken;
import project.tripplan.domain.auth.refreshToken.repository.BlackListRepository;
import project.tripplan.domain.auth.refreshToken.repository.RefreshTokenRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.UserRole;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JWTService {
	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	/**
	 * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
	 * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
	 */
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String BEARER = "Bearer ";

	private final UserRepositoryCustom userRepositoryCustom;
	private final RefreshTokenRepository refreshTokenRepository;
	private final BlackListRepository blackListRepository;

	/**
	 * AccessToken 생성 메소드
	 */
	public String createAccessToken(String socialId, UserRole userRole) {
		Date now = new Date();

		String accessToken = JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
			.withClaim("social_id", socialId)
			.withClaim("role", userRole.name())
			.sign(Algorithm.HMAC512(secretKey));

		log.info("accessToken 발급 완료");
		log.info("발급된 accessToken:{}", accessToken);
		return accessToken;
	}



	/**
	 * RefreshToken 생성
	 * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
	 */
	public String createRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * AccessToken 헤더에 실어서 보내기
	 */
	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader(accessHeader, accessToken);
		log.info("재발급된 Access Token : {}", accessToken);
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
		log.info("발급된 accessToken : {}", accessToken);
		log.info("발급된 refreshToken : {}", refreshToken);
		log.info("Access Token, Refresh Token 헤더 설정 완료");
	}

	/**
	 * 헤더에서 RefreshToken 추출
	 * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
	 * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
	 */
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	/**
	 * 헤더에서 AccessToken 추출
	 * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
	 * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
	 */
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		log.info("extractAccessToken 실행");
		return Optional.ofNullable(request.getHeader(accessHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	/**
	 * AccessToken에서 socialId 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	public Optional<Map<String, Object>> extractSocialId(String accessToken) {
		// JWT 검증 및 클레임 추출
		var decodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
			.build() // JWT Verifier 생성
			.verify(accessToken); // accessToken 검증

		// socialId 추출
		String socialId = decodedJWT.getClaim("social_id").asString();

		if (socialId != null) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("social_id", socialId); // socialId를 클레임에 추가
			return Optional.of(claims);
		} else {
			log.error("social_id 클레임이 존재하지 않습니다.");
			return Optional.empty();
		}
	}




	/**
	 * AccessToken 헤더 설정
	 */
	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	/**
	 * RefreshToken 헤더 설정
	 */
	public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	/**
	 * RefreshToken DB 저장(업데이트)
	 */
	public void updateRefreshToken(User user, String socialId, String refreshToken) {
		// 기존 RefreshToken 조회
		Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findById(socialId);

		if (findRefreshToken.isPresent()) {
			// RefreshToken이 존재하면 업데이트
			findRefreshToken.get().updateRefreshToken(refreshToken);
			log.info("사용자 {}의 Refresh Token이 업데이트되었습니다.", user.getNickname());
		} else {
			// RefreshToken이 존재하지 않으면 새로 생성 후 저장
			RefreshToken newRefreshToken = new RefreshToken(user.getSocialId(), refreshToken,
				refreshTokenExpirationPeriod);
			refreshTokenRepository.save(newRefreshToken);
			log.info("사용자 {}의 Refresh Token이 새로 생성되었습니다.", user.getNickname());
		}
	}

	/**
	 * AccessToken에서 만료 시간 추출
	 */
	public Optional<Long> extractAccessExpiration(String accessToken) {
		try {
			// JWT 검증 및 클레임 추출
			var decodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
				.build() // JWT Verifier 생성
				.verify(accessToken); // accessToken 검증

			// 만료 시간 추출 (밀리초 단위)
			Date expirationDate = decodedJWT.getExpiresAt();

			// 밀리초를 초 단위로 변환하여 반환
			return Optional.of(expirationDate.getTime() / 1000);
		} catch (Exception e) {
			log.error("만료 시간을 추출하는 데 실패했습니다: {}", e.getMessage());
			return Optional.empty();
		}
	}

	public boolean isTokenValid(String token) { // Token 유효성 검증
		try {
			log.info("isTokenValid 실행");
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}

	public boolean isAccessTokenBlacklisted(String accessToken) {
		Optional<BlackList> findAccessToken = blackListRepository.findById(accessToken);
		return findAccessToken.isPresent();
	}

}
