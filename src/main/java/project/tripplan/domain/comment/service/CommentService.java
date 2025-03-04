package project.tripplan.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.comment.dto.CommentReq;
import project.tripplan.domain.comment.entity.PlanComment;
import project.tripplan.domain.comment.repository.PlanCommentRepository;
import project.tripplan.domain.comment.repository.PlanCommentRepositoryCustom;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.UserRole;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

	private final PlanCommentRepository planCommentRepository;
	private final PlanRepository planRepository;
	private final PlanCommentRepositoryCustom planCommentRepositoryCustom;

	@Transactional
	public Long addComment(User user, Long planId, CommentReq commentReq) {
		Plan findPlan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		PlanComment planComment = PlanComment.builder()
			.user(user)
			.plan(findPlan)
			.content(commentReq.getContent())
			.build();

		return planCommentRepository.save(planComment).getId();
	}

	@Transactional
	public void deleteComment(User user, Long commentId) {
		PlanComment findPlanComment = planCommentRepositoryCustom.findByIdWithUser(commentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		if(findPlanComment.getUser().getId() != user.getId() && user.getUserRole() != UserRole.ADMIN) {
			// 관리자가 아니면서 본인 댓글이 아닌 댓글을 삭제하려는 경우
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_DELETE_COMMENT);
		}

		planCommentRepository.delete(findPlanComment);
	}

	@Transactional
	public void updateComment(User user, Long commentId, CommentReq commentReq) {
		PlanComment findPlanComment = planCommentRepositoryCustom.findByIdWithUser(commentId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.COMMENT_NOT_EXIST));

		if(findPlanComment.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_UPDATE_COMMENT);
		}

		findPlanComment.updateComment(commentReq.getContent());
	}

}
