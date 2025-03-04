package project.tripplan.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.tripplan.domain.comment.dto.ReviewCommentReq;
import project.tripplan.domain.comment.entity.ReviewComment;
import project.tripplan.domain.comment.repository.ReviewCommentRepository;
import project.tripplan.domain.comment.repository.ReviewCommentRepositoryCustom;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@ExtendWith(MockitoExtension.class)
class ReviewPlanCommentServiceTest {

	@Mock
	private ReviewCommentRepositoryCustom reviewCommentRepositoryCustom;

	@Mock
	private ReviewCommentRepository reviewCommentRepository;

	@Mock
	private ReviewRepository reviewRepository;

	@InjectMocks
	private ReviewCommentService reviewCommentService;

	@Test
	@DisplayName("addComment : 정상적으로 리뷰 코멘트를 추가하면, 저장된 ReviewComment의 ID를 반환한다.")
	void addComment_Success() {
		// given
		Long reviewId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		ReviewCommentReq req = new ReviewCommentReq("Test Content");

		// 가정: 리뷰(Review)가 존재
		Review review = Review.builder().id(reviewId).build();

		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

		// ReviewComment 저장 시, DB에서 100L 이라는 ID가 부여된다고 가정
		ReviewComment savedEntity = ReviewComment.builder()
			.id(100L).build();

		when(reviewCommentRepository.save(any(ReviewComment.class))).thenReturn(savedEntity);

		// when
		Long result = reviewCommentService.addComment(user, reviewId, req);

		// then
		assertEquals(100L, result); // 반환된 ID가 100인지 확인
		verify(reviewRepository, times(1)).findById(reviewId);
		verify(reviewCommentRepository, times(1)).save(any(ReviewComment.class));
	}

	@Test
	@DisplayName("addComment : 존재하지 않는 리뷰에 코멘트를 달 경우 예외가 발생한다.")
	void addComment_ReviewNotExist() {
		// given
		Long reviewId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		ReviewCommentReq req = new ReviewCommentReq("Test Content");

		// 리뷰가 존재하지 않는 경우
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

		// when & then
		CustomException ex = assertThrows(CustomException.class, () ->
			reviewCommentService.addComment(user, reviewId, req)
		);
		assertEquals(BaseResponseCode.REVIEW_NOT_EXIST, ex.getBaseResponseCode());
		verify(reviewCommentRepository, never()).save(any(ReviewComment.class));
	}

	@Test
	@DisplayName("deleteReviewComment : 본인 댓글이면 정상적으로 삭제된다.")
	void deleteReviewComment_Success() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		// 댓글의 작성자도 user와 동일(10L)이라고 설정
		User commentOwner = User.builder().id(10L).build();
		// 가정: DB에 이미 저장된 댓글
		ReviewComment comment = ReviewComment.builder().user(commentOwner).build();

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.of(comment));

		// when
		reviewCommentService.deleteReviewComment(user, commentId);

		// then
		verify(reviewCommentRepositoryCustom, times(1)).findByIdWithUser(commentId);
		verify(reviewCommentRepository, times(1)).delete(comment);
	}

	@Test
	@DisplayName("deleteReviewComment : 다른 사람이 작성한 댓글을 삭제하려 하면 예외가 발생한다.")
	void deleteReviewComment_Unauthorized() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		// 댓글의 작성자 user(20L)이라고 설정
		User commentOwner = User.builder().id(20L).build();
		ReviewComment comment = ReviewComment.builder().user(commentOwner).build();

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.of(comment));

		// when & then
		CustomException ex = assertThrows(CustomException.class, () ->
			reviewCommentService.deleteReviewComment(user, commentId)
		);
		assertEquals(BaseResponseCode.UNAUTHORIZED_DELETE_COMMENT, ex.getBaseResponseCode());
		// 삭제 시도도 하지 않음
		verify(reviewCommentRepository, never()).delete(any());
	}

	@Test
	@DisplayName("deleteReviewComment : 존재하지 않는 댓글일 경우 예외가 발생한다.")
	void deleteReviewComment_NotExist() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.empty());

		// when & then
		CustomException ex = assertThrows(CustomException.class, () ->
			reviewCommentService.deleteReviewComment(user, commentId)
		);
		assertEquals(BaseResponseCode.COMMENT_NOT_EXIST, ex.getBaseResponseCode());
	}

	@Test
	@DisplayName("updateReviewComment : 본인 댓글이면 정상적으로 수정된다.")
	void updateReviewComment_Success() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		ReviewCommentReq commentReq = new ReviewCommentReq("Updated Content");

		User commentOwner = User.builder().id(10L).build();
		ReviewComment comment = ReviewComment.builder().user(commentOwner).build();

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.of(comment));

		// when
		reviewCommentService.updateReviewComment(user, commentId, commentReq);

		// then
		assertEquals("Updated Content", comment.getContent());
		verify(reviewCommentRepositoryCustom, times(1)).findByIdWithUser(commentId);
	}

	@Test
	@DisplayName("updateReviewComment : 다른 사람이 작성한 댓글을 수정하려 하면 예외가 발생한다.")
	void updateReviewComment_Unauthorized() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		ReviewCommentReq commentReq = new ReviewCommentReq("Updated Content");

		// 댓글의 작성자는 ID = 20L
		User commentOwner = User.builder().id(20L).build();
		ReviewComment comment = ReviewComment.builder().user(commentOwner).build();

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.of(comment));

		// when & then
		CustomException ex = assertThrows(CustomException.class, () ->
			reviewCommentService.updateReviewComment(user, commentId, commentReq)
		);
		assertEquals(BaseResponseCode.UNAUTHORIZED_UPDATE_COMMENT, ex.getBaseResponseCode());
		// 내용이 바뀌지 않았는지 확인(실제 엔티티 안에 getContent()가 존재한다고 가정)
		assertNotEquals("Updated Content", comment.getContent());
	}

	@Test
	@DisplayName("updateReviewComment : 존재하지 않는 댓글이면 예외가 발생한다.")
	void updateReviewComment_NotExist() {
		// given
		Long commentId = 1L;
		User user = User.builder()
			.id(10L)
			.build();

		ReviewCommentReq commentReq = new ReviewCommentReq("Updated Content");

		when(reviewCommentRepositoryCustom.findByIdWithUser(commentId))
			.thenReturn(Optional.empty());

		// when & then
		CustomException ex = assertThrows(CustomException.class, () ->
			reviewCommentService.updateReviewComment(user, commentId, commentReq)
		);
		assertEquals(BaseResponseCode.COMMENT_NOT_EXIST, ex.getBaseResponseCode());
	}

}
