package project.tripplan.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.QReviewComment;
import project.tripplan.domain.comment.entity.ReviewComment;
import project.tripplan.domain.review.entity.QReview;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewCommentRepositoryImpl implements ReviewCommentRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QReviewComment reviewComment = QReviewComment.reviewComment;
	private final QUser user = QUser.user;
	private final QReview review = QReview.review;

	@Override
	public Optional<ReviewComment> findByIdWithUser(Long commentId) {
		return Optional.ofNullable(
			qf.select(reviewComment)
				.from(reviewComment)
				.join(reviewComment.user, user).fetchJoin()
				.where(reviewComment.id.eq(commentId))
				.fetchOne()
		);
	}

	@Override
	public Page<ReviewComment> findAllByReviewIdWithUser(Long reviewId, Pageable pageable) {
		// 쿼리 생성
		List<ReviewComment> content = qf
			.selectFrom(reviewComment)
			.join(reviewComment.review, review).fetchJoin()
			.join(reviewComment.user, user).fetchJoin()
			.where(review.id.eq(reviewId))
			.orderBy(reviewComment.createdAt.desc(), reviewComment.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 총 개수 계산
		Long totalCount = qf
			.select(reviewComment.count())
			.from(reviewComment)
			.where(review.id.eq(reviewId))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		// PageImpl을 사용해 Page 객체 반환
		return new PageImpl<>(content, pageable, total);
	}

}
