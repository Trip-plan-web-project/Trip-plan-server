package project.tripplan.domain.auth.refreshToken.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash(value = "blackList")
public class BlackList {
	@Id
	private String accessToken;
	@TimeToLive
	private Long expiration;

	public BlackList(String accessToken, Long expiration) {
		this.accessToken = accessToken;
		this.expiration = expiration;
	}
}
