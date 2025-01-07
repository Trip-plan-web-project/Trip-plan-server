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
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private User user;

    private String title;

    @Column(name = "view_count")
    private Long viewCount;

    private Long people;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private PlanStatus status;

    @Column(name = "total_cost")
    private Long totalCost;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "plan")
    private List<PlanDay> planDays = new ArrayList<>();

}
