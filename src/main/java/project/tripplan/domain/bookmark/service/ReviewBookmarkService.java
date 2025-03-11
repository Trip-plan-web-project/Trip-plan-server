package project.tripplan.domain.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.entity.ReviewBookmark;
import project.tripplan.domain.bookmark.repository.ReviewBookmarkRepository;
import project.tripplan.domain.bookmark.repository.ReviewBookmarkRepositoryCustom;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewBookmarkService {

	private final ReviewBookmarkRepository reviewBookmarkRepository;
	private final ReviewBookmarkRepositoryCustom reviewBookmarkRepositoryCustom;
	private final ReviewRepository reviewRepository;

	public Long addReviewBookmark(User user, Long reviewId) {
		Review findReview = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_NOT_EXIST));

		ReviewBookmark reviewBookmark = ReviewBookmark.builder()
			.user(user)
			.review(findReview)
			.build();

		return reviewBookmarkRepository.save(reviewBookmark).getId();
	}

	public void deleteReviewBookmark(User user, Long reviewBookmarkId) {
		ReviewBookmark findReviewBookmark = reviewBookmarkRepositoryCustom.findByReviewBookmarkIdWithUserId(
				reviewBookmarkId, user.getId())
			.orElseThrow(() -> new CustomException(BaseResponseCode.BOOKMARK_NOT_EXIST));

		// 사용자가 다른 경우
		if (findReviewBookmark.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.REVIEW_BOOKMARK_NOT_EXIST);
		}

		reviewBookmarkRepository.delete(findReviewBookmark);
	}

}
