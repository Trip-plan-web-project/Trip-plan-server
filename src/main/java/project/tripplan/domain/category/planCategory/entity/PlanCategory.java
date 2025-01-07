package project.tripplan.domain.category.planCategory.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.category.planCategory.enums.PlanCategoryName;
import project.tripplan.global.common.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class PlanCategory extends BaseEntity {
    @Id
    @Column(name = "plan_category_id")
    private Long planCategoryId;

    @Enumerated(EnumType.STRING)
    private PlanCategoryName name;

    @Column(columnDefinition = "TEXT")
    private String image;
}
