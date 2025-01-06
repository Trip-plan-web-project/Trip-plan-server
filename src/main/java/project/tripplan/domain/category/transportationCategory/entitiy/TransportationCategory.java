package project.tripplan.domain.category.transportationCategory.entitiy;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class TransportationCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transport_category_id")
    private Long transportCategoryId;

    @Enumerated(EnumType.STRING)
    private TransportationName name;
}
