// package project.tripplan.global.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
//
// import lombok.RequiredArgsConstructor;
//
// @Configuration
// @EnableWebSecurity
// @RequiredArgsConstructor
// public class securityConfig {
// 	private final JWTService jwtService;
// 	private final Oauth2SuccessHandler Oauth2SuccessHandler;
// 	private final Oauth2FailureHandler Oauth2FailureHandler;
// 	private final CustomOAuth2UserService customOAuth2UserService;
//
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http
// 			.csrf(CsrfConfigurer::disable)
// 			.formLogin(auth -> auth.disable())
// 			.httpBasic(auth -> auth.disable())
// 			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 			.oauth2Login(oauth2 -> oauth2
// 				.userInfoEndpoint(userInfo -> userInfo
// 					.userService(customOAuth2UserService) // 사용자 정보 처리를 위한 커스텀 서비스
// 				)
// 				.successHandler(Oauth2SuccessHandler) // 인증 성공 핸들러
// 				.failureHandler(Oauth2FailureHandler) // 인증 실패 핸들러
// 			)
//
//
// 	}
// }
