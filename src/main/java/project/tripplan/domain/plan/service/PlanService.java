package project.tripplan.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepository;
import project.tripplan.domain.category.planCategory.repository.PlanCategoryRepository;
import project.tripplan.domain.plan.dto.DayPlanDto;
import project.tripplan.domain.plan.dto.DetailDto;
import project.tripplan.domain.plan.dto.PlanDto;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.file.FileStore;
import project.tripplan.domain.plan.file.UploadFile;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepository;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.planDay.repository.PlanDayRepository;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.domain.planDayDetail.repository.PlanDayDetailRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

import static project.tripplan.domain.plan.enums.PlanStatus.PUBLIC;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlanService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PlanCategoryRepository planCategoryRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlanPlaceCategoryRepository planPlaceCategoryRepository;
    private final FileStore fileStore;

    /**
     * 계획 저장 메서드
     *
     * @param planDto    계획 정보 DTO
     * @param thumbnail  썸네일 이미지 파일
     * @return 저장된 Plan의 ID
     * @throws IOException 파일 저장 중 예외 발생 시
     */
    public Boolean savePlan(PlanDto planDto, MultipartFile thumbnail) throws IOException {

        // 1) 사용자 조회
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: 1L"));

        // 2) 총 비용 계산
        long totalCost = calculateTotalCost(planDto);

        // 3) 우선 Plan 엔티티를 생성하되, imageUrl은 아직 null(또는 빈 값)로
        Plan plan = Plan.builder()
                .user(user)
                .title(planDto.getTitle())
                .subtitle(planDto.getSubtitle())
                .people(planDto.getPeople())
                .status(PUBLIC)
                .totalCost(totalCost)
                .startDate(planDto.getStartDate())
                .endDate(planDto.getEndDate())
                .build();

        // 4) DB에 먼저 저장(영속화). 이 시점부터 plan은 영속 상태
        Plan savedPlan = planRepository.save(plan);

        // 5) PlanDay + PlanDayDetail 저장
        savePlanDayAndPlanDayDetail(planDto, savedPlan);

        // 6) PlaceCategory / PlanPlaceCategory 저장
        savePlaceCategory(planDto, savedPlan);

        // 7) 마지막에 파일(썸네일) 저장
        if (thumbnail != null && !thumbnail.isEmpty()) {
            UploadFile savedThumbnail = fileStore.storeFile(thumbnail);
            savedPlan.setImageUrl(savedThumbnail.getStoreFileName());
        }

        return true;
    }

    private void savePlanDayAndPlanDayDetail(PlanDto planDto, Plan plan) {
        // PlanDay 및 PlanDayDetail 엔티티 생성 및 연관관계 설정
        if (planDto.getDays() != null && !planDto.getDays().isEmpty()) {
            for (DayPlanDto dayPlanDto : planDto.getDays()) {
                PlanDay planDay = PlanDay.builder()
                        .day(dayPlanDto.getDay())
                        .cost(dayPlanDto.getCost())
                        .date(dayPlanDto.getDate())
                        .build();

                // PlanDayDetail 생성 및 카테고리 이름 추가
                if (dayPlanDto.getDetail() != null && !dayPlanDto.getDetail().isEmpty()) {
                    for (DetailDto detailDto : dayPlanDto.getDetail()) {
                        String categoryName = detailDto.getPlanCategoryName();

                        // 카테고리 이름이 존재하는지 검증
                        if (!planCategoryRepository.existsById(categoryName)) {
                            throw new IllegalArgumentException("category name: " + categoryName + "없음");
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

                        // 카테고리 이름 추가
//                        planDayDetail.addPlanCategoryName(categoryName);

                        // PlanDay에 PlanDayDetail 추가
                        planDay.addPlanDayDetail(planDayDetail);
                    }
                }

                // Plan에 PlanDay 추가
                plan.addPlanDay(planDay);
            }
        }
    }

    private void savePlaceCategory(PlanDto planDto, Plan plan) {
        //카테고리
        List<String> categoryList = planDto.getCategory();
        if (categoryList != null && !categoryList.isEmpty()) {
            // (1) 계층 구조로 저장 or 조회
            PlaceCategory finalCategory = upsertCategories(categoryList);

            // (2) PlanPlaceCategory로 Plan과 최하위 카테고리 연결
            if (finalCategory != null) {
                PlanPlaceCategory planPlaceCategory = PlanPlaceCategory.builder()
                        .plan(plan)
                        .placeCategory(finalCategory)
                        .build();
                planPlaceCategoryRepository.save(planPlaceCategory);
            }
        }
    }

    /**
     * 총 비용을 계산하는 메서드
     */
    private long calculateTotalCost(PlanDto planDto) {
        long totalCost = 0;
        if (planDto.getDays() != null && !planDto.getDays().isEmpty()) {
            for (DayPlanDto dayPlan : planDto.getDays()) {
                totalCost += dayPlan.getCost();
            }
        } else {
            log.info("dto에 days 비어있음.");
        }
        return totalCost;
    }


    /**
     * ex) ["경기도", "안산시", "선부동"]를 순회하며,
     * parent + name 으로 찾고, 없으면 생성.
     * 최하위 카테고리 반환
     */
    private PlaceCategory upsertCategories(List<String> categoryList) {
        PlaceCategory parent = null;
        int depth = 0;

        for (String categoryName : categoryList) {
            // 1) parent+name으로 조회
            PlaceCategory placeCategory = placeCategoryRepository.findByNameAndParent(categoryName, parent);

            // 2) 없으면 생성
            if (placeCategory == null) {
                placeCategory = PlaceCategory.builder()
                        .name(categoryName)
                        .depth(depth)
                        .parent(parent)
                        .build();
                placeCategoryRepository.save(placeCategory);
            }

            // 다음 루프에서 parent로 사용
            parent = placeCategory;
            depth++;
        }

        // 가장 마지막에 생성 or 조회된 카테고리가 최하위
        return parent;
    }
}
