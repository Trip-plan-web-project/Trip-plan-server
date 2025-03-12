package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.entity.QReviewBookmark;
import project.tripplan.domain.bookmark.entity.ReviewBookmark;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewBookmarkRepositoryCustomImpl implements ReviewBookmarkRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QReviewBookmark reviewBookmark = QReviewBookmark.reviewBookmark;
	private final QUser user = QUser.user;

	@Override
	public Optional<ReviewBookmark> findByReviewBookmarkIdWithUserId(Long reviewBookmarkId, Long userId) {
		return Optional.ofNullable(
			qf.selectFrom(reviewBookmark)
				.join(reviewBookmark.user, user).fetchJoin()
				.where(reviewBookmark.user.id.eq(userId)
					.and(reviewBookmark.id.eq(reviewBookmarkId)))
				.fetchOne()
		);
	}

	@Override
	public Optional<ReviewBookmark> findByReviewIdAndUserId(Long reviewId, Long userId) {
		return Optional.ofNullable(
			qf.selectFrom(reviewBookmark)
				.join(reviewBookmark.user, user).fetchJoin()
				.where(reviewBookmark.review.id.eq(reviewId)
					.and(reviewBookmark.user.id.eq(userId)))
				.fetchOne()
		);
	}

}
