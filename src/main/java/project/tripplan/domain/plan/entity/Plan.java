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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "plan")
    private List<PlanDay> planDays = new ArrayList<>();

}
