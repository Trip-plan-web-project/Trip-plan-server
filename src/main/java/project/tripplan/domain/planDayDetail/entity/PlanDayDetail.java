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
    @Column(name = "Key")
    private String keyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "plandetail_id")
    private PlanDay planDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_category_id")
    private PlanCategory planCategory;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "place_name")
    private String placeName;
    @Column(name = "street_address")
    private String streetAddress;

    private Double latitude;
    private Double longitude;

}
