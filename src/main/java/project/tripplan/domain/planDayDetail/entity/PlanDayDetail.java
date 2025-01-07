package project.tripplan.domain.planDayDetail.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.category.planCategory.entity.PlanCategory;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class PlanDayDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_day_detail_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_category_id")
    private PlanCategory planCategory;

    private int orderIndex;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

}
