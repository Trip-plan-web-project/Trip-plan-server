package project.tripplan.domain.planDayDetail.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.planDayDetail.dto.PlanDetailDayRes;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.domain.planDayDetail.repository.PlanDayDetailRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanDayDetailService {

	private final PlanDayDetailRepositoryCustom planDayDetailRepositoryCustom;

	@Transactional(readOnly = true)
	public PlanDetailDayRes getPlanDayDetail(Integer day, Long planId) {
		List<PlanDayDetail> findAllPlanDayDetail = planDayDetailRepositoryCustom.findAllByPlanIdWithPlanCategory(
			planId, day);

		if (findAllPlanDayDetail.isEmpty()) {
			throw new CustomException(BaseResponseCode.PLAN_DAY_DETAIL_NOT_EXIST);
		}

		List<PlanDetailDayRes.PlaceDetail> places = findAllPlanDayDetail.stream()
			.map(planDayDetail -> new PlanDetailDayRes.PlaceDetail(
				planDayDetail.getOrderIndex(),
				planDayDetail.getPlaceName(),
				planDayDetail.getStreetAddress(),
				planDayDetail.getPlanCategory().getCode(),
				planDayDetail.getLatitude(),
				planDayDetail.getLongitude()
			))
			.toList();

		PlanDayDetail planDayDetail = findAllPlanDayDetail.get(0);

		return new PlanDetailDayRes(planDayDetail.getPlanDay().getDate(), planDayDetail.getPlanDay().getCost(), day,
			places);
	}
}
