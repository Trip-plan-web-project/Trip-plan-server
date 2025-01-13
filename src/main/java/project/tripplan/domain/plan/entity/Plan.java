package project.tripplan.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.planDay.entity.PlanDay;
import project.tripplan.domain.planLike.entity.PlanLike;
import project.tripplan.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Plan {
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
}
