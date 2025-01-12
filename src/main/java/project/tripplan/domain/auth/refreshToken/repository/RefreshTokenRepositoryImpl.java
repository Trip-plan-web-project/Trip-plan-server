package project.tripplan.domain.auth.refreshToken.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.auth.refreshToken.entity.QRefreshToken;
import project.tripplan.domain.auth.refreshToken.entity.RefreshToken;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QRefreshToken rToken = QRefreshToken.refreshToken1;
	private final QUser user = QUser.user;

	@Override
	public Optional<RefreshToken> findByRefreshToken(String token) {
		return Optional.ofNullable(
			qf.selectFrom(rToken)
				.join(rToken.user, user).fetchJoin()
				.where(rToken.refreshToken.eq(token))
				.fetchOne()
		);
	}

	@Override
	public Optional<RefreshToken> findByUserId(Long userId) {
		return Optional.ofNullable(
			qf.selectFrom(rToken)
				.where(rToken.user.id.eq(userId))
				.fetchOne()
		);
	}

}
