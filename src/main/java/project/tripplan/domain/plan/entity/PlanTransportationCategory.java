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
    @Column(name = "Key")
    private Long keyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_category_id")
    private TransportationCategory transportCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;


}
