package project.tripplan.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.category.transportationCategory.entitiy.TransportationCategory;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class PlanTransportationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_transportation_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportation_category_id")
    private TransportationCategory transportationCategory;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;


}
