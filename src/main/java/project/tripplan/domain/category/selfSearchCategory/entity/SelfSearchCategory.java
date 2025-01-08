package project.tripplan.domain.category.selfSearchCategory.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.global.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class SelfSearchCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "self_search_category_id")
    private Long id;

    private String name;

    private int depth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private SelfSearchCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfSearchCategory> children = new ArrayList<>();

}
