package project.tripplan.domain.review.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.review.entity.QReview;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.user.entity.QUser;

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
}
