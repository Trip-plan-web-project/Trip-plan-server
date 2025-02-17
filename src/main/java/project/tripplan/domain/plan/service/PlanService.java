package project.tripplan.domain.plan.service;

import static project.tripplan.domain.plan.enums.PlanStatus.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.service.PlaceCategoryService;
import project.tripplan.domain.category.planCategory.entity.PlanCategory;
import project.tripplan.domain.category.planCategory.repository.PlanCategoryRepository;
import project.tripplan.domain.category.transportationCategory.entitiy.TransportationCategory;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;
import project.tripplan.domain.category.transportationCategory.repository.TransportationCategoryRepository;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepositoryCustom;
import project.tripplan.domain.plan.dto.HomeRes;
import project.tripplan.domain.plan.dto.PlaceCategoryNamesReq;
import project.tripplan.domain.plan.dto.PlanCommentsRes;
import project.tripplan.domain.plan.dto.PlanContentAndTotalCountRes;
import project.tripplan.domain.plan.dto.PlanDataReq;
import project.tripplan.domain.plan.dto.PlanDayDetailReq;
import project.tripplan.domain.plan.dto.PlanDayReq;
import project.tripplan.domain.plan.dto.PlanDetailRes;
import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.dto.PlanNoOffsetRes;
import project.tripplan.domain.plan.dto.PlanReq;
import project.tripplan.domain.plan.dto.PlanSearchRes;
import project.tripplan.domain.plan.dto.PlanStatusReq;
import project.tripplan.domain.plan.dto.PlanUpdateReq;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.entity.PlanTransportationCategory;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepository;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanTransCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanTransportationCategoryRepository;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.domain.planLike.entity.PlanLike;
import project.tripplan.domain.planLike.repository.PlanLikeRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlanService {

	@Value("${cloud.prefix}")
	private String prefix;

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
	private final CommentRepositoryCustom commentRepositoryCustom;
	private final UserRepository userRepository;

	/**
	 * 계획 저장 메서드
	 */
	@Transactional
	public Boolean savePlan(User user, PlanReq planReq, MultipartFile thumbnail) throws IOException {
		Plan plan = Plan.builder()
			.user(user)
			.status(PUBLIC)
			.build();

		applyPlanData(plan, planReq);

		if (thumbnail != null && !thumbnail.isEmpty()) {
			String savedThumbnail = s3Service.uploadFile(thumbnail);
			plan.setImageUrl(savedThumbnail);
		}

		planRepository.save(plan);

		return true;
	}

	@Transactional
	public void updatePlanStatus(User user, Long planId, @Valid PlanStatusReq planStatusReq) {
		Plan findPlan = planRepositoryCustom.findByPlanIdWithUser(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		if (findPlan.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_POST_UPDATE_STATUS);
		}

		findPlan.updateStatus(planStatusReq.getStatus());
	}

	@Transactional
	public PlanDetailRes getPlanInfoDetails(User user, Long planId) {
		List<PlanPlaceCategory> findPlanPlaceCategories = planPlaceCategoryRepositoryCustom.findAllByPlanIdWithPlanAndPlace(
			planId);

		Plan findPlan = planRepositoryCustom.findByPlanIdWithUser(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		Optional<PlanLike> findPlanLike = planLikeRepositoryCustom.findPlanLikeWithUserAndPlan(user.getId(),
			planId);

		Long likesCount = planLikeRepositoryCustom.countLikesByPlanId(planId);

		//조회수 증가
		findPlan.increaseViewCount();

		PlanTransportationCategory findPlanTrans = planTransCategoryRepositoryCustom.findByPlanIdWithPlanTransCategory(
				planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.GET_PLAN_TRANS_FAIL));

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
		planDetailRes.setSocialId(findPlan.getUser().getSocialId());
		planDetailRes.setLikeId(findPlanLike.orElse(null) != null ? findPlanLike.get().getId() : null);
		planDetailRes.setPlaceCategory(categoryNames);
		planDetailRes.setAuthor(findPlan.getUser().getNickname());
		planDetailRes.setProfileImage(prefix + "/" + findPlan.getUser().getImage());
		planDetailRes.setThumbnail(prefix + "/" + findPlan.getImageUrl());
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

	@Transactional(readOnly = true)
	public PlanNoOffsetRes getPlanNoOffset(PlanNoOffsetReq req) {

		// 1) categoryNames가 있는 경우, 자식까지 포함한 categoryIds 구하기 (OR 조건)
		if (req.getCategoryNames() != null && !req.getCategoryNames().isEmpty()) {
			Set<Long> allIds = placeCategoryService.findAllDescendantCategoryIds(req.getCategoryNames());
			req.setCategoryNamecategoryIds(allIds);
		}

		// 2) keyword가 지역으로 판별되는 경우, placeCategory까지 검색
		String keyword = req.getKeyword();
		if (keyword != null && !keyword.isEmpty()) {
			// isLocationKeyword(keyword)
			Set<Long> categoryIds = placeCategoryService.findAllSearchDescendantCategoryIds(keyword);
			req.setTitleCategoryIds(categoryIds);
		}

		// 2) DB 조회 (size+1 개)
		PlanContentAndTotalCountRes rawList = planRepositoryCustom.searchPlanNoOffset(req);

		// 3) hasNext (size 이상이면 다음 페이지 존재)
		boolean hasNext = rawList.getContent().size() > req.getSize();

		// 4) 실제 반환 목록 (size까지만)
		List<Plan> content = hasNext
			? rawList.getContent().subList(0, req.getSize())
			: rawList.getContent();

		// 5) nextValue, nextId 설정
		String nextValue = null;
		Long nextId = null;
		if (!content.isEmpty()) {
			Plan lastPlan = content.get(content.size() - 1);

			// 전통 switch
			switch (req.getSortBy()) {
				case "viewCount":
					// null 안전 처리
					long vc = (lastPlan.getViewCount() == null) ? 0L : lastPlan.getViewCount();
					nextValue = String.valueOf(vc);
					break;

				case "id":
				default:
					nextValue = String.valueOf(lastPlan.getId());
					break;
			}
			nextId = lastPlan.getId();
		}

		// 6) DTO 변환
		List<PlanSearchRes> plans = content.stream()
			.map(plan -> new PlanSearchRes(plan, prefix))
			.toList();

		// 7) 응답 구성
		PlanNoOffsetRes response = new PlanNoOffsetRes();
		response.setPlans(plans);
		response.setHasNext(hasNext);
		response.setNextValue(nextValue);
		response.setNextId(nextId);
		response.setTotalCount(rawList.getTotalCount());

		return response;
	}

	@Transactional(readOnly = true)
	public HomeRes getHome() {
		List<Plan> findMostViewed = planRepositoryCustom.findMostViewedPlans(10);
		List<Plan> findMostRecent = planRepositoryCustom.findMostRecentPlans(10);
		List<PlanPlaceCategory> findHotPlaces = planPlaceCategoryRepositoryCustom.findHotPlacesByPlaceName("강남", 10);

		// planId 추출
		List<Long> mostViewedPlanIds = findMostViewed.stream()
			.map(Plan::getId)
			.toList();

		List<Long> mostRecentPlanIds = findMostRecent.stream()
			.map(Plan::getId)
			.toList();

		// PlaceCategory와 TransportationCategory 데이터 조회
		List<PlanPlaceCategory> mostViewedPlaces = planPlaceCategoryRepositoryCustom.findAllByPlanIds(
			mostViewedPlanIds);
		List<PlanPlaceCategory> mostRecentPlaces = planPlaceCategoryRepositoryCustom.findAllByPlanIds(
			mostRecentPlanIds);

		List<PlanTransportationCategory> mostViewedTrans = planTransCategoryRepositoryCustom.findAllByPlanIds(
			mostViewedPlanIds);
		List<PlanTransportationCategory> mostRecentTrans = planTransCategoryRepositoryCustom.findAllByPlanIds(
			mostRecentPlanIds);

		// 핫플레이스에 필요한 데이터 조회
		List<Long> hotPlacePlanIds = findHotPlaces.stream()
			.map(planPlaceCategory -> planPlaceCategory.getPlan().getId())
			.toList();

		List<PlanTransportationCategory> hotPlacesTrans = planTransCategoryRepositoryCustom.findAllByPlanIds(
			hotPlacePlanIds);

		// DTO 변환
		List<HomeRes.PlanInfo> mostViewedPlans = convertToPlanInfo(findMostViewed, mostViewedPlaces, mostViewedTrans);
		List<HomeRes.PlanInfo> mostRecentPlans = convertToPlanInfo(findMostRecent, mostRecentPlaces, mostRecentTrans);
		List<HomeRes.PlanInfo> hotPlacePlans = convertHotPlacesToPlanInfo(findHotPlaces, hotPlacesTrans);

		// 결과 반환
		return new HomeRes(mostViewedPlans, mostRecentPlans, hotPlacePlans);
	}

	private List<HomeRes.PlanInfo> convertToPlanInfo(
		List<Plan> plans,
		List<PlanPlaceCategory> placeCategories,
		List<PlanTransportationCategory> transCategories
	) {
		// PlaceCategory와 TransportationCategory를 Plan ID 기준으로 그룹화
		Map<Long, List<String>> placeCategoryMap = placeCategories.stream()
			.collect(Collectors.groupingBy(
				p -> p.getPlan().getId(),
				Collectors.mapping(p -> p.getPlaceCategory().getName(), Collectors.toList())
			));

		Map<Long, String> transCategoryMap = transCategories.stream()
			.collect(Collectors.toMap(
				t -> t.getPlan().getId(),
				t -> t.getTransportationCategory().getName().toString(), // Enum 타입을 String으로 변환
				(existing, replacement) -> existing // 중복 발생 시 첫 번째 값 유지
			));

		// Plan 데이터를 기반으로 DTO 생성
		return plans.stream()
			.map(plan -> new HomeRes.PlanInfo(
				plan.getId(),
				plan.getTitle(),
				placeCategoryMap.getOrDefault(plan.getId(), List.of()), // PlaceCategory가 없으면 빈 리스트 반환
				plan.getStartDate(),
				plan.getEndDate(),
				plan.getPeople(),
				transCategoryMap.getOrDefault(plan.getId(), null), // TransportationCategory가 없으면 null 반환
				plan.getTotalCost().intValue(),
				prefix + "/" + plan.getImageUrl()
			))
			.toList();
	}

	private List<HomeRes.PlanInfo> convertHotPlacesToPlanInfo(
		List<PlanPlaceCategory> placeCategories,
		List<PlanTransportationCategory> transCategories
	) {
		// TransportationCategory를 Plan ID 기준으로 그룹화
		Map<Long, String> transCategoryMap = transCategories.stream()
			.collect(Collectors.toMap(
				t -> t.getPlan().getId(),
				t -> t.getTransportationCategory().getName().toString(), // Enum 타입을 String으로 변환
				(existing, replacement) -> existing // 중복 발생 시 첫 번째 값 유지
			));

		// PlanPlaceCategory 데이터를 기반으로 DTO 생성
		return placeCategories.stream()
			.map(placeCategory -> {
				Plan plan = placeCategory.getPlan();
				return new HomeRes.PlanInfo(
					plan.getId(),
					plan.getTitle(),
					List.of(placeCategory.getPlaceCategory().getName()), // 단일 PlaceCategory만 포함
					plan.getStartDate(),
					plan.getEndDate(),
					plan.getPeople(),
					transCategoryMap.getOrDefault(plan.getId(), null), // TransportationCategory가 없으면 null 반환
					plan.getTotalCost().intValue(),
					prefix + "/" + plan.getImageUrl()
				);
			})
			.toList();
	}

	@Transactional(readOnly = true)
	public Page<PlanCommentsRes> getPlanComments(Long planId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Comment> findCommentsPage = commentRepositoryCustom.findAllByPlanIdWithUser(planId, pageable);

		return findCommentsPage.map(comment -> new PlanCommentsRes(
			comment.getUser().getSocialId(),
			comment.getId(),
			comment.getUser().getNickname(),
			comment.getCreatedAt(),
			comment.getContent()
		));
	}

	public void deletePlan(Long planId, Long userId) {
		Plan plan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));
		log.info("userId = {}", plan.getUser().getId());
		if (plan.getUser().getId() != userId) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_POST_DELETE_STATUS);
		}
		planRepository.delete(plan);
	}

	public void copyPlan(Long planId, Long userId) {
		Plan plan = planRepositoryCustom.findPlanWithAllChildren(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

		Plan copyPlan = Plan.builder()
			.user(user)
			.title(plan.getTitle())
			.viewCount(0L)
			.people(plan.getPeople())
			.status(PlanStatus.PRIVATE)
			.totalCost(plan.getTotalCost())
			.startDate(plan.getStartDate())
			.endDate(plan.getEndDate())
			.planTransportationCategories(new HashSet<>())
			.planPlaceCategories(new HashSet<>())
			.planDays(new HashSet<>())
			.planLikes(new ArrayList<>())
			.comments(new ArrayList<>())
			.build();

		planRepository.save(copyPlan);
		copyRelatedEntities(plan, copyPlan);
		copyPlan.setImageUrl(s3Service.copyFile(plan.getImageUrl()));
	}

	private void copyRelatedEntities(Plan plan, Plan copyPlan) {
		// 1) PlanPlaceCategory copy
		for (PlanPlaceCategory ppc : plan.getPlanPlaceCategories()) {
			PlanPlaceCategory cppc = PlanPlaceCategory.builder()
				.plan(copyPlan)
				.placeCategory(ppc.getPlaceCategory())
				.build();

			copyPlan.getPlanPlaceCategories().add(cppc);
		}

		// 2) PlanTransportationCategory copy
		for (PlanTransportationCategory ptc : plan.getPlanTransportationCategories()) {
			PlanTransportationCategory cptc = PlanTransportationCategory.builder()
				.plan(copyPlan)
				.transportationCategory(ptc.getTransportationCategory())
				.build();

			copyPlan.getPlanTransportationCategories().add(cptc);
		}

		// 3) PlanDay + PlanDayDetail copy
		for (PlanDay pd : plan.getPlanDays()) {
			PlanDay cpd = PlanDay.builder()
				.plan(copyPlan)
				.day(pd.getDay())
				.date(pd.getDate())
				.cost(pd.getCost())
				.planDayDetails(new HashSet<>())
				.build();

			// PlanDayDetail copy
			copyPlanDayDetails(pd, cpd);

			copyPlan.getPlanDays().add(cpd);
		}
	}

	private void copyPlanDayDetails(PlanDay planDay, PlanDay copyPlanDay) {
		for (PlanDayDetail pdd : planDay.getPlanDayDetails()) {
			PlanDayDetail cpdd = PlanDayDetail.builder()
				.planDay(copyPlanDay)
				.orderIndex(pdd.getOrderIndex())
				.placeName(pdd.getPlaceName())
				.streetAddress(pdd.getStreetAddress())
				.latitude(pdd.getLatitude())
				.longitude(pdd.getLongitude())
				.planCategory(pdd.getPlanCategory())
				.build();

			copyPlanDay.getPlanDayDetails().add(cpdd);
		}
	}

	@Transactional
	public Boolean updatePlan(User user, PlanUpdateReq planUpdateReq, MultipartFile thumbnail) throws IOException {
		Plan plan = planRepository.findById(planUpdateReq.getPlanId())
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		if (!plan.getUser().getId().equals(user.getId())) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_POST_UPDATE_STATUS);
		}

		//plan에 연결된 planDay, planPlaceCategory, transportation 삭제
		plan.clearAllPlanDays();
		plan.clearAllPlanPlaceCategories();
		plan.clearAllTransportationCategories();

		if (planUpdateReq.getStatus() != null) {
			plan.setStatus(planUpdateReq.getStatus());
		}

		/**
		 * plan 정보 input
		 */
		applyPlanData(plan, planUpdateReq);

		//이미지 변경시 삭제 후 재 생성
		if (thumbnail != null && !thumbnail.isEmpty()) {
			s3Service.deleteFile(plan.getImageUrl());
			plan.setImageUrl(s3Service.uploadFile(thumbnail));
		}

		return true;
	}

	/**
	 * savePlan, updatePlan 공통 로직
	 */
	private void applyPlanData(Plan plan, PlanDataReq dto) {

		//plan
		plan.applyPlanBasicFields(dto.getTitle(),
			dto.getPeople(),
			dto.getStartDate(),
			dto.getEndDate(),
			calculateTotalCostFromDays(dto.getDays()));

		//planDay
		if (dto.getDays() != null) {
			for (PlanDayReq dayReq : dto.getDays()) {
				PlanDay planDay = PlanDay.builder()
					.day(dayReq.getDay())
					.cost(dayReq.getCost())
					.date(dayReq.getDate())
					.plan(plan)
					.build();

				//planDayDetail
				if (dayReq.getDetail() != null) {
					for (PlanDayDetailReq detailReq : dayReq.getDetail()) {
						PlanCategory planCategory = planCategoryRepository.findById(detailReq.getPlanCategoryNameId())
							.orElseThrow(() -> new CustomException(BaseResponseCode.CATEGORY_NOT_EXIST));

						PlanDayDetail planDayDetail = PlanDayDetail.builder()
							.orderIndex(detailReq.getOrder())
							.placeName(detailReq.getPlace())
							.streetAddress(detailReq.getStreetAddress())
							.latitude(detailReq.getLatitude())
							.longitude(detailReq.getLongitude())
							.planCategory(planCategory)
							.planDay(planDay)
							.build();

						planDay.getPlanDayDetails().add(planDayDetail);
					}
				}

				plan.getPlanDays().add(planDay);
			}
		}

		//PlaceCategory 연결
		if (dto.getCategory() != null) {
			for (PlaceCategoryNamesReq categoryReq : dto.getCategory()) {
				PlaceCategory finalCategory = placeCategoryService.searchPlaceCategory(categoryReq);
				if (finalCategory != null) {
					PlanPlaceCategory planPlaceCategory = PlanPlaceCategory.builder()
						.plan(plan)
						.placeCategory(finalCategory)
						.build();
					plan.getPlanPlaceCategories().add(planPlaceCategory);
				}
			}
		}

		//TransportationCategory 연결
		if (dto.getTransportation() != null) {
			TransportationName name = TransportationName.valueOf(dto.getTransportation());
			TransportationCategory transportationCategory =
				transportationCategoryRepository.findByName(name)
					.orElseThrow(() -> new CustomException(BaseResponseCode.GET_PLAN_TRANS_FAIL));

			PlanTransportationCategory ptc = PlanTransportationCategory.builder()
				.plan(plan)
				.transportationCategory(transportationCategory)
				.build();
			plan.getPlanTransportationCategories().add(ptc);
		}
	}

	private long calculateTotalCostFromDays(List<PlanDayReq> days) {
		if (days == null || days.isEmpty()) {
			return 0;
		}
		return days.stream()
			.mapToLong(PlanDayReq::getCost)
			.sum();
	}

	// 지역 검색용 키워드인지 판단하는 메서드 예시
	private boolean isLocationKeyword(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return false;
		}

		if (keyword.endsWith("시") || keyword.endsWith("구") ||
			keyword.endsWith("동") || keyword.endsWith("읍") ||
			keyword.endsWith("면")) {
			return true;
		}

		return false;
	}

}
