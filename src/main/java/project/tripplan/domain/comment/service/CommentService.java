package project.tripplan.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.comment.dto.CommentReq;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepository;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PlanRepository planRepository;

	@Transactional
	public Long addComment(User user, Long planId, CommentReq commentReq) {
		Plan findPlan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		Comment comment = Comment.builder()
			.user(user)
			.plan(findPlan)
			.content(commentReq.getContent())
			.build();

		return commentRepository.save(comment).getId();
	}

	@Transactional
	public void deleteComment(User user, Long commentId) {
		Comment findComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		commentRepository.delete(findComment);
	}

	@Transactional
	public void updateComment(User user, Long commentId, CommentReq commentReq) {
		Comment findComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		findComment.updateComment(commentReq.getContent());
	}

}
