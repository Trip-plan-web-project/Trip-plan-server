package project.tripplan.domain.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.user.entity.QUser;
import project.tripplan.domain.user.entity.User;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QUser user = QUser.user;
	private final QPlan plan = QPlan.plan;
	private final QPlanPlaceCategory planPlaceCategory = QPlanPlaceCategory.planPlaceCategory;

	@Override
	public Optional<User> findBySocialId(String socialId) {
		return Optional.ofNullable(
			qf.selectFrom(user)
				.where(user.socialId.eq(socialId))
				.fetchOne()
		);
	}

}
