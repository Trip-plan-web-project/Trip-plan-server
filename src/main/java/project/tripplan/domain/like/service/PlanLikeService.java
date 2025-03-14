package project.tripplan.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.like.entity.PlanLike;
import project.tripplan.domain.like.repository.PlanLikeRepository;
import project.tripplan.domain.like.repository.PlanLikeRepositoryCustom;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlanLikeService {

	private final PlanLikeRepositoryCustom planLikeRepositoryCustom;
	private final PlanLikeRepository planLikeRepository;
	private final PlanRepository planRepository;

	public Long addPlanLike(User user, Long planId) {
		Plan findPlan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		PlanLike planLike = PlanLike.builder()
			.user(user)
			.plan(findPlan)
			.build();

		findPlan.addPlanLikes(planLike);

		planLikeRepository.save(planLike);

		return planLike.getId();
	}

	public void deletePlanLike(User user, Long planLikeId) {
		PlanLike findPlanLike = planLikeRepositoryCustom.findPlanLikeWithUserAndPlan(planLikeId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLANLIKE_NOT_EXIST));

		if (findPlanLike.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_PLANLIKE_DELETE);
		}
		findPlanLike.getPlan().getPlanLikes().remove(findPlanLike);

		planLikeRepository.delete(findPlanLike);
	}
}
