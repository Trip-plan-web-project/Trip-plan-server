package project.tripplan.global.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.Provider;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;
import project.tripplan.global.jwt.JWTService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepositoryCustom userRepositoryCustom;
	private final UserRepository userRepository;
	private final JWTService jwtService;

	private static final String KAKAO = "kakao";
	private static final String NAVER = "naver";

	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		try {
			/**
			 * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
			 * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
			 * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
			 * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
			 */
			OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
			OAuth2User oAuth2User = delegate.loadUser(userRequest);

			/**
			 * userRequest에서 registrationId 추출 후 registrationId으로 Provider 저장
			 * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
			 * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
			 */
			String registrationId = userRequest.getClientRegistration().getRegistrationId();
			Provider provider = getProvider(registrationId);
			String userNameAttributeName = userRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
			Map<String, Object> attributes = oAuth2User.getAttributes();

			OAuthAttributes extractAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);
			User createUser = getUser(extractAttributes, provider);

			return new CustomOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(createUser.getUserRole().toString())),
				attributes,
				extractAttributes.getNameAttributeKey(),
				createUser.getSocialId()
			);

		} catch (OAuth2AuthenticationException ex) {
			log.error("[loadUser] OAuth2AuthenticationException 발생: {}", ex.getMessage(), ex);
			// OAuth2AuthenticationException을 CustomException으로 래핑
			throw new CustomException(BaseResponseCode.INVALID_OAUTH2_REQUEST);
		} catch (Exception ex) {
			log.error("[loadUser] 예기치 않은 예외 발생: {}", ex.getMessage(), ex);
			throw new CustomException(BaseResponseCode.INTERNAL_SERVER_ERROR);
		}
	}

	private Provider getProvider(String registrationId) {
		if (KAKAO.equalsIgnoreCase(registrationId)) {
			return Provider.KAKAO;
		} else if (NAVER.equalsIgnoreCase(registrationId)) {
			return Provider.NAVER;
		} else {
			return Provider.GOOGLE;
		}
	}

	/**
	 * Provider와 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
	 * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveUser()를 호출하여 회원을 저장한다.
	 */
	private User getUser(OAuthAttributes attributes, Provider provider) {
		log.info("getUser 메서드로 회원 조회 ");

		Optional<User> findUser = userRepositoryCustom.findBySocialId(attributes.getOauth2UserInfo().getId());

		if (findUser.isPresent()) {
			log.info("이미 존재하는 유저 검색 완료: {}", findUser.get().getNickname());
			return findUser.get(); // 기존 유저 반환
		} else {
			log.info("해당 유저가 존재하지 않아 OAUTH2로 새로 회원을 저장합니다.");
			return saveUser(attributes, provider); // 새로운 유저 저장 및 반환
		}
	}

	/**
	 * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 Member 객체 생성 후 반환
	 * 생성된 Member 객체를 DB에 저장 : Provider, socialId, email, role 값만 있는 상태
	 */
	private User saveUser(OAuthAttributes attributes, Provider provider) {
		User createdUser = attributes.toEntity(provider, attributes.getOauth2UserInfo());
		userRepository.save(createdUser);
		log.info(" OAUTH 회원 저장 완료 ");
		return createdUser;
	}
}
