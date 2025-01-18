package project.tripplan.domain.plan.service;

import static project.tripplan.domain.plan.enums.PlanStatus.*;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.service.PlaceCategoryService;
import project.tripplan.domain.category.planCategory.repository.PlanCategoryRepository;
import project.tripplan.domain.category.transportationCategory.entitiy.TransportationCategory;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;
import project.tripplan.domain.category.transportationCategory.repository.TransportationCategoryRepository;
import project.tripplan.domain.plan.dto.DayPlanReq;
import project.tripplan.domain.plan.dto.DetailReq;
import project.tripplan.domain.plan.dto.PlaceCategoryNamesReq;
import project.tripplan.domain.plan.dto.PlanDetailRes;
import project.tripplan.domain.plan.dto.PlanReq;
import project.tripplan.domain.plan.dto.PlanStatusReq;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.entity.PlanTransportationCategory;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepository;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanTransCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanTransportationCategoryRepository;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.domain.planLike.repository.PlanLikeRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlanService {

	private final PlanRepository planRepository;
	private final PlanCategoryRepository planCategoryRepository;
	private final PlanPlaceCategoryRepository planPlaceCategoryRepository;
	private final PlanPlaceCategoryRepositoryCustom planPlaceCategoryRepositoryCustom;
	private final PlanTransCategoryRepositoryCustom planTransCategoryRepositoryCustom;
	private final PlanRepositoryCustom planRepositoryCustom;
	private final PlanLikeRepositoryCustom planLikeRepositoryCustom;
	private final S3Service s3Service;
	private final PlaceCategoryService placeCategoryService;
	private final TransportationCategoryRepository transportationCategoryRepository;
	private final PlanTransportationCategoryRepository planTransportationCategoryRepository;

	/**
	 * 계획 저장 메서드
	 */
	public Boolean savePlan(User user, PlanReq planReq, MultipartFile thumbnail) throws IOException {

		// 1) 총 비용 계산
		long totalCost = calculateTotalCost(planReq);

		// 2) 우선 Plan 엔티티를 생성하되, imageUrl은 아직 null(또는 빈 값)로
		// 이미지는 마지막에 추가할 예정.
		Plan plan = Plan.builder()
			.user(user)
			.title(planReq.getTitle())
			.subtitle(planReq.getSubtitle())
			.people(planReq.getPeople())
			.status(PUBLIC)
			.totalCost(totalCost)
			.startDate(planReq.getStartDate())
			.endDate(planReq.getEndDate())
			.build();

		// 3) DB에 먼저 저장(영속화). 이 시점부터 plan은 영속 상태
		Plan savedPlan = planRepository.save(plan);

		// 4) PlanDay + PlanDayDetail 저장
		savePlanDayAndPlanDayDetail(planReq, savedPlan);

		// 5) PlaceCategory / PlanPlaceCategory 저장
		savePlaceCategory(planReq, plan);

		// 6) TransportationCategory / PlanTransportationCategory 저장
		saveTransportationCategory(planReq, savedPlan);

		// 7) 마지막에 파일(썸네일) 저장
		if (thumbnail != null && !thumbnail.isEmpty()) {
			String savedThumbnail = s3Service.uploadFile(thumbnail);
			savedPlan.setImageUrl(savedThumbnail);
		}

		return true;
	}

	private void savePlaceCategory(PlanReq planReq, Plan plan) {
		for (PlaceCategoryNamesReq categoryReq : planReq.getCategory()) {
			// 부모/자식 카테고리 찾거나 생성
			PlaceCategory finalCategory = placeCategoryService.searchPlaceCategory(categoryReq);

			// Plan + Category 연결
			if (finalCategory != null) {
				PlanPlaceCategory planPlaceCategory = PlanPlaceCategory.builder()
					.plan(plan)
					.placeCategory(finalCategory)
					.build();
				planPlaceCategoryRepository.save(planPlaceCategory);
			}
		}
	}

	private void savePlanDayAndPlanDayDetail(PlanReq planDto, Plan plan) {
		// PlanDay 및 PlanDayDetail 엔티티 생성 및 연관관계 설정
		if (planDto.getDays() != null && !planDto.getDays().isEmpty()) {
			for (DayPlanReq dayPlanDto : planDto.getDays()) {
				PlanDay planDay = PlanDay.builder()
					.day(dayPlanDto.getDay())
					.cost(dayPlanDto.getCost())
					.date(dayPlanDto.getDate())
					.build();

				// PlanDayDetail 생성 및 카테고리 이름 추가
				if (dayPlanDto.getDetail() != null && !dayPlanDto.getDetail().isEmpty()) {
					for (DetailReq detailDto : dayPlanDto.getDetail()) {
						String categoryName = detailDto.getPlanCategoryName();

						// 카테고리 이름이 존재하는지 검증
						if (!planCategoryRepository.existsById(categoryName)) {
							throw new CustomException(BaseResponseCode.CATEGORY_NOT_EXIST);
						}

						// PlanDayDetail 엔티티 생성
						PlanDayDetail planDayDetail = PlanDayDetail.builder()
							.orderIndex(detailDto.getOrder())
							.placeName(detailDto.getPlace())
							.planCategoryName(detailDto.getPlanCategoryName())
							.streetAddress(detailDto.getStreetAddress())
							.latitude(detailDto.getLatitude())
							.longitude(detailDto.getLongitude())
							.build();

						// PlanDay에 PlanDayDetail 추가
						planDay.addPlanDayDetail(planDayDetail);
					}
				}

				// Plan에 PlanDay 추가
				plan.addPlanDay(planDay);
			}
		}
	}

	/**
	 * 총 비용을 계산하는 메서드
	 */
	private long calculateTotalCost(PlanReq planDto) {
		long totalCost = 0;
		if (planDto.getDays() != null && !planDto.getDays().isEmpty()) {
			for (DayPlanReq dayPlan : planDto.getDays()) {
				totalCost += dayPlan.getCost();
			}
		} else {
			log.info("dto에 days 비어있음.");
		}
		return totalCost;
	}

	@Transactional
	public void updatePlanStatus(User user, Long planId, @Valid PlanStatusReq planStatusReq) {
		Plan findPlan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		findPlan.updateStatus(planStatusReq.getStatus());
	}

	@Transactional(readOnly = true)
	public PlanDetailRes getPlanInfoDetails(User user, Long planId) {
		List<PlanPlaceCategory> findPlanPlaceCategories = planPlaceCategoryRepositoryCustom.findAllByPlanIdWithPlanAndPlace(
			planId);

		Plan findPlan = planRepositoryCustom.findByPlanIdWithUser(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		PlanTransportationCategory findPlanTrans = planTransCategoryRepositoryCustom.findByPlanIdWithPlanTransCategory(
				planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.GET_PLAN_TRANS_FAIL));

		Long likesCount = planLikeRepositoryCustom.countLikesByPlanId(planId);

		if (findPlanPlaceCategories.isEmpty()) {
			throw new CustomException(BaseResponseCode.GET_PLAN_PLACE_FAIL);
		}

		List<String> categoryNames = findPlanPlaceCategories.stream()
			.map(planPlaceCategory -> planPlaceCategory.getPlaceCategory().getName())
			.toList();

		// 첫 번째 PlanPlaceCategory에서 공통 정보를 가져옴
		PlanPlaceCategory placeCategory = findPlanPlaceCategories.get(0);

		// PlanDetailRes DTO 생성
		PlanDetailRes planDetailRes = new PlanDetailRes();
		planDetailRes.setTitle(placeCategory.getPlan().getTitle());
		planDetailRes.setPlaceCategory(categoryNames);
		planDetailRes.setAuthor(findPlan.getUser().getNickname());
		planDetailRes.setProfileImage(findPlan.getUser().getImage());
		planDetailRes.setCreatedAt(placeCategory.getPlan().getCreatedAt());
		planDetailRes.setStartDate(placeCategory.getPlan().getStartDate());
		planDetailRes.setEndDate(placeCategory.getPlan().getEndDate());
		planDetailRes.setStatus(placeCategory.getPlan().getStatus());
		planDetailRes.setViewCount(placeCategory.getPlan().getViewCount());
		planDetailRes.setLike(likesCount);
		planDetailRes.setPeople(placeCategory.getPlan().getPeople());
		planDetailRes.setTransportation(findPlanTrans.getTransportationCategory().getName());
		planDetailRes.setTotalCost(placeCategory.getPlan().getTotalCost());

		return planDetailRes;
	}

	private void saveTransportationCategory(PlanReq planReq, Plan plan) {
		String transportationNameStr = planReq.getTransportation();
		TransportationName transportationName = TransportationName.valueOf(transportationNameStr);
		TransportationCategory transportationCategory =
			transportationCategoryRepository.findByName(transportationName).get();

		PlanTransportationCategory planTransportationCategory =
			PlanTransportationCategory.builder()
				.transportationCategory(transportationCategory)
				.plan(plan)
				.build();
		planTransportationCategoryRepository.save(planTransportationCategory);
	}
}
