package project.tripplan.domain.auth.refreshToken.entity;

import java.util.Random;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RedisHash(value = "RefreshToken")
public class RefreshToken {
	@Id
	private String id;

	@Indexed
	private String refreshToken;

	@TimeToLive
	private Long expiration;

	@Indexed
	private String socialId;

	public RefreshToken(String socialId, String refreshToken, Long expiration) {
		this.id = UUID.randomUUID().toString();
		this.refreshToken = refreshToken;
		this.expiration = expiration;
		this.socialId = socialId;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
