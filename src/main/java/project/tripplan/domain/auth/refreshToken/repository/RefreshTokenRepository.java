package project.tripplan.domain.auth.refreshToken.repository;

import java.sql.Ref;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import project.tripplan.domain.auth.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findByUserId(Long userId);

	Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
