package project.tripplan.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.review.entity.QReview;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.user.entity.QUser;
import project.tripplan.domain.user.entity.User;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory qf;

	private final QReview review = QReview.review;
	private final QUser user = QUser.user;

	@Override
	public Optional<Review> findReviewIdWithUser(Long reviewId) {

		return Optional.ofNullable(
			qf.selectFrom(review)
				.join(review.user, user).fetchJoin()
				.where(review.id.eq(reviewId))
				.fetchOne()
		);
	}

	@Override
	public List<Review> findByPlaceIdAndUserNot(String placeId, User user) {
		return qf.selectFrom(review)
			.join(review.user, this.user).fetchJoin()
			.where(review.placeId.eq(placeId)
				.and(review.user.ne(user)))
			.orderBy(review.createdAt.desc())
			.fetch();
	}

	@Override
	public List<Review> findByAllReview() {
		return qf.selectFrom(review)
			.join(review.user, user).fetchJoin()
			.orderBy(review.createdAt.desc())
			.fetch();
	}
}
