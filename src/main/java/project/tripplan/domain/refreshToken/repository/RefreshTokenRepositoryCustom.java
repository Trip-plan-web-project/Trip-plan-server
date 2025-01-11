package project.tripplan.domain.refreshToken.repository;

import java.util.Optional;

import project.tripplan.domain.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepositoryCustom {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findByUserId(Long userId);
}
