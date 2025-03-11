package project.tripplan.domain.like.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.like.entity.QReviewLike;
import project.tripplan.domain.like.entity.ReviewLike;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepositoryCustomImpl implements ReviewLikeRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QReviewLike reviewLike = QReviewLike.reviewLike;
	private final QUser user = QUser.user;

	@Override
	public Optional<ReviewLike> findReviewLikeWithUser(Long reviewLikeId) {
		return Optional.ofNullable(
			qf.selectFrom(reviewLike)
				.join(reviewLike.user, user).fetchJoin()
				.where(reviewLike.id.eq(reviewLikeId))
				.fetchOne()
		);
	}
}
