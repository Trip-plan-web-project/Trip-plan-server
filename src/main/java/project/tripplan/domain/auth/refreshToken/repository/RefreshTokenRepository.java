package project.tripplan.domain.auth.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.auth.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
