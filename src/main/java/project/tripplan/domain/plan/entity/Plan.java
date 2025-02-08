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
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.planLike.entity.PlanLike;
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

	@Builder.Default
	private Long viewCount = 0L;

	@Column(nullable = false)
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

	@Builder.Default
	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanTransportationCategory> planTransportationCategories = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanPlaceCategory> planPlaceCategories = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlanDay> planDays = new HashSet<>();

	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlanLike> planLikes = new ArrayList<>();

	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

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

	public void increaseViewCount() {
		this.viewCount = this.viewCount + 1;
	}

	public String getFirstTransportCategoryName() {
		return planTransportationCategories.stream()
			.findFirst()  // Optional<PlanTransportationCategory>
			.map(ptc -> ptc.getTransportationCategory().getName().toString())
			.orElse(null); // 없으면 null
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public void setStatus(PlanStatus status) {
		this.status = status;
	}

	public void setTotalCost(Long totalCost) {
		this.totalCost = totalCost;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void clearAllPlanDays() {
		for (PlanDay planDay : this.planDays) {
			planDay.setPlan(null);
		}
		this.planDays.clear();
	}

	public void clearAllPlanPlaceCategories() {
		for (PlanPlaceCategory ppc : this.planPlaceCategories) {
			ppc.setPlan(null);
		}
		this.planPlaceCategories.clear();
	}

	public void clearAllTransportationCategories() {
		for (PlanTransportationCategory ptc : this.planTransportationCategories) {
			ptc.setPlan(null);
		}
		this.planTransportationCategories.clear();
	}
}
