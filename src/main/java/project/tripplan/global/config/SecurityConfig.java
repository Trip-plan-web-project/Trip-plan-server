package project.tripplan.global.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.auth.service.AuthService;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.jwt.JWTService;
import project.tripplan.global.jwt.JwtAuthenticationProcessingFilter;
import project.tripplan.global.oauth.CustomOAuth2UserService;
import project.tripplan.global.oauth.OAuth2LoginFailureHandler;
import project.tripplan.global.oauth.OAuth2LoginSuccessHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JWTService jwtService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final UserRepositoryCustom userRepositoryCustom;
	private final AuthService authService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
			.csrf(CsrfConfigurer::disable)
			.formLogin(auth -> auth.disable())
			.httpBasic(auth -> auth.disable())
			.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/")
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
				)
				.successHandler(oAuth2LoginSuccessHandler)
				.failureHandler(oAuth2LoginFailureHandler)
			)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/home", "/plans/search", "/login", "/test", "/token/issue/**", "/token/reissue/**",
					"/",
					"/index.html", "/favicon.ico",
					"/plans",
					"/plans/{planId}/copy",
					"/review",
					"review/{reviewId}",
					"/review/image/upload"
				).permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest()
				.authenticated()
			)
			.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration config = new CorsConfiguration();

			config.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
			config.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
			config.setAllowedOriginPatterns(Arrays.asList(
				"http://localhost:3000",
				"https://trip-plan-frontend.vercel.app"
			)); // 허용할 Origin 리스트
			config.setAllowCredentials(true);

			return config;
		};
	}

	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		return new JwtAuthenticationProcessingFilter(jwtService,
			userRepositoryCustom);
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(authService);
		return new ProviderManager(provider);
	}
}
