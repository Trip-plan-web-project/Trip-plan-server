package project.tripplan.domain.plan.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Plan extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String title;

	private Long viewCount;

	@Column(nullable = false)
	private String subtitle;

	private int people;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private PlanStatus status;

	@Column(nullable = false)
	private Long totalCost;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanTransportationCategory> planTransportationCategories = new HashSet<>();

	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanPlaceCategory> planPlaceCategories = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlanDay> planDays = new ArrayList<>();

	public void addPlanDay(PlanDay planDay) {
		planDays.add(planDay);
		planDay.setPlan(this);
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void updateStatus(PlanStatus status) {
		this.status = status;
	}

	public String getFirstTransportCategoryName() {
		return planTransportationCategories.stream()
			.findFirst()  // Optional<PlanTransportationCategory>
			.map(ptc -> ptc.getTransportationCategory().getName().toString())
			.orElse(null); // 없으면 null
	}
}
