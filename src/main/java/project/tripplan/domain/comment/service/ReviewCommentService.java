package project.tripplan.domain.comment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.comment.dto.ReviewCommentReq;
import project.tripplan.domain.comment.entity.ReviewComment;
import project.tripplan.domain.comment.repository.ReviewCommentRepository;
import project.tripplan.domain.comment.repository.ReviewCommentRepositoryCustom;
import project.tripplan.domain.plan.dto.PlanCommentsRes;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.UserRole;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewCommentService {

	@Value("${cloud.prefix}")
	private String prefix;

	private final ReviewCommentRepositoryCustom reviewCommentRepositoryCustom;
	private final ReviewCommentRepository reviewCommentRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public Long addComment(User user, Long planId, ReviewCommentReq reviewCommentReq) {
		Review review = reviewRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_NOT_EXIST));

		ReviewComment reviewComment = ReviewComment.builder()
			.user(user)
			.review(review)
			.content(reviewCommentReq.getContent())
			.build();

		return reviewCommentRepository.save(reviewComment).getId();
	}

	@Transactional
	public void deleteReviewComment(User user, Long reviewCommentId) {
		ReviewComment findReviewComment = reviewCommentRepositoryCustom.findByIdWithUser(reviewCommentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		if (findReviewComment.getUser().getId() != user.getId() && user.getUserRole() != UserRole.ADMIN) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_DELETE_COMMENT);
		}

		reviewCommentRepository.delete(findReviewComment);
	}

	@Transactional
	public void updateReviewComment(User user, Long reviewCommentId, ReviewCommentReq reviewCommentReq) {
		ReviewComment reviewComment = reviewCommentRepositoryCustom.findByIdWithUser(reviewCommentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		if (reviewComment.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_UPDATE_COMMENT);
		}

		reviewComment.updateComment(reviewCommentReq.getContent());
	}

	@Transactional(readOnly = true)
	public Page<PlanCommentsRes> getPlanComments(Long reviewId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ReviewComment> findCommentsPage = reviewCommentRepositoryCustom.findAllByReviewIdWithUser(reviewId,
			pageable);

		return findCommentsPage.map(comment -> new PlanCommentsRes(
			comment.getUser().getSocialId(),
			comment.getId(),
			prefix + "/" + comment.getUser().getImage(),
			comment.getUser().getNickname(),
			comment.getCreatedAt(),
			comment.getContent()
		));
	}
}
