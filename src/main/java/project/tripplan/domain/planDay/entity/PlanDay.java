package project.tripplan.domain.planDay.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class PlanDay extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_day_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id")
	private Plan plan;

	private int day;

	@Column(nullable = false)
	private LocalDate date;

	private int cost;

	@Builder.Default
	@OneToMany(mappedBy = "planDay", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanDayDetail> planDayDetails = new HashSet<>();

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public void addPlanDayDetail(PlanDayDetail planDayDetail) {
		planDayDetails.add(planDayDetail);
		planDayDetail.setPlanDay(this);
	}
}
