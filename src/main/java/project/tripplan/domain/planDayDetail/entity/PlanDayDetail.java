package project.tripplan.domain.planDayDetail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	@ManyToOne
	@JoinColumn(name = "plan_category_id")
	private PlanCategory planCategory;

	public void setPlanDay(PlanDay planDay) {
		this.planDay = planDay;
	}
}
