package project.tripplan.domain.planDayDetail.entity;

import jakarta.persistence.*;
import lombok.*;
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
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_day_id")
	private PlanDay planDay;

    private int orderIndex;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private String streetAddress;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

    @Column(nullable = false)
    private String planCategoryName;

    public void setPlanDay(PlanDay planDay) {
        this.planDay = planDay;
    }



}
