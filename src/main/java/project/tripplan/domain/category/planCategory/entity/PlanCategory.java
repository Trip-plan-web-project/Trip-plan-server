package project.tripplan.domain.category.planCategory.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.global.common.entity.BaseEntity;



@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Table(name = "plan_category")
public class PlanCategory extends BaseEntity {

    @Id
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image;
}