package project.tripplan.global.oauth;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
	public NaverOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		// response 객체 내부의 id를 가져옴
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		if (response == null) {
			return null;
		}
		return (String) response.get("id");
	}

	@Override
	public String getNickname() {
		// response 객체 내부의 nickname을 가져옴
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		if (response == null) {
			return null;
		}
		return (String) response.get("name");
	}
}
