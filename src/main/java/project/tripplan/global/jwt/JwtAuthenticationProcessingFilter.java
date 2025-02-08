package project.tripplan.global.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
	private final JWTService jwtService;
	private final UserRepositoryCustom userRepositoryCustom;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	@Transactional
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {
		// 사용자 요청 헤더에서 RefreshToken 추출
		// -> RefreshToken 이 없거나 유효하지 않다면(DB에 저장된 RefreshToken 과 다르다면) null 을 반환
		// 사용자의 요청 헤더에 RefreshToken 이 있는 경우는, AccessToken 이 만료되어 요청한 경우밖에 없다.
		// 따라서, 위의 경우를 제외하면 추출한 refreshToken 은 모두 null
		String refreshToken = jwtService.extractRefreshToken(request)
			.filter(jwtService::isTokenValid)
			.orElse(null);

		// RefreshToken 이 없거나 유효하지 않다면, AccessToken 을 검사하고 인증을 처리하는 로직 수행
		// AccessToken 이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
		// AccessToken 이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
		checkAccessTokenAndAuthentication(request, response, filterChain);
	}

	/**
	 * [액세스 토큰 체크 & 인증 처리 메소드]
	 * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
	 * 유효한 토큰이면, 액세스 토큰에서 extract로 추출
	 * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
	 * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
	 * 그 후 다음 인증 필터로 진행
	 */
	public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		log.info("checkAccessTokenAndAuthentication() 호출");

		// Access Token 추출 및 유효성 검증
		boolean isAccessTokenValid = jwtService.extractAccessToken(request)
			.filter(accessToken -> {
				// 2️⃣ AccessToken이 블랙리스트에 있는지 확인
				if (jwtService.isAccessTokenBlacklisted(accessToken)) {
					log.warn("블랙리스트에 등록된 AccessToken 사용 시도 - {}", accessToken);
					try {
						sendUnauthorizedResponse(response, "Access Token is blacklisted");
					} catch (IOException e) {
						log.error("IOException 발생: {}", e.getMessage());
						throw new RuntimeException("Failed to send unauthorized response");
					}
					return false;
				}
				return true;
			})
			.filter(jwtService::isTokenValid)
			.map(accessToken -> {
				jwtService.extractSocialId(accessToken).ifPresent(claims -> {
					String socialId = (String)claims.get("social_id");
					log.info("Extracted socialId : {}", socialId);

					User findUser = userRepositoryCustom.findBySocialId(socialId)
						.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

					log.info("Found user for socialId : {}", socialId);
					saveAuthentication(findUser);
				});
				return true;
			})
			.orElse(false);

		if (!isAccessTokenValid) {
			// Access Token이 없거나 유효하지 않으면 401 Unauthorized 반환
			log.error("Access Token이 유효하지 않습니다");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Access Token is expired or invalid");
			response.getWriter().flush();
			return; // 필터 체인 중단
		}

		// Access Token이 유효한 경우, 다음 필터로 진행
		filterChain.doFilter(request, response);
	}

	/**
	 * [인증 허가 메소드]
	 * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
	 *
	 * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
	 * UsernamePasswordAuthenticationToken의 파라미터
	 * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
	 * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
	 * 3. Collection < ? extends GrantedAuthority>로,
	 * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
	 * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
	 *
	 * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
	 * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
	 */
	public void saveAuthentication(User user) {
		// User 엔티티의 정보를 기반으로 UserDetails 객체 생성
		UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
			.username(user.getSocialId()) // 소셜 로그인에서는 socialId를 username으로 사용
			.password("")
			.roles(user.getUserRole().name())
			.build();

		// Authentication 객체 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			user, // User 엔티티를 직접 principal로 설정
			null,
			authoritiesMapper.mapAuthorities(
				Collections.singletonList(
					new SimpleGrantedAuthority(user.getUserRole().name()) // userRole을 기반으로 권한 생성
				)
			)
		);

		// SecurityContextHolder에 인증 정보 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * [401 Unauthorized 응답을 전송하는 메서드]
	 */
	private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		log.error(message);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(message);
		response.getWriter().flush();
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String requestURI = request.getRequestURI();

		boolean isExcluded = requestURI.equals("/") ||
			requestURI.equals("/login") ||
			requestURI.equals("/home") ||
			requestURI.startsWith("/plans/search") ||
			requestURI.startsWith("/test") ||
			requestURI.startsWith("/token/issue/") ||
			requestURI.startsWith("/token/reissue/") ||
			requestURI.startsWith("/index.html") ||
			requestURI.startsWith("/favicon.ico");

		log.info("Request URI: {} | Should not filter: {}", requestURI, isExcluded);

		return isExcluded;
	}
}
