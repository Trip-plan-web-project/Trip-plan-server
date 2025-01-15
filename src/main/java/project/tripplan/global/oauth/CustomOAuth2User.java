package project.tripplan.global.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;
import project.tripplan.domain.user.enums.Provider;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

	private String socialId;

	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes, String nameAttributeKey,
		String socialId) {
		super(authorities, attributes, nameAttributeKey);
		this.socialId = socialId;
	}
}
