package project.tripplan.domain.planDay.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.global.common.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<PlanDayDetail> planDayDetails = new ArrayList<>();


    public void setPlan(Plan plan) {
        this.plan = plan;
    }


    public void addPlanDayDetail(PlanDayDetail planDayDetail) {
        planDayDetails.add(planDayDetail);
        planDayDetail.setPlanDay(this);
    }
}
