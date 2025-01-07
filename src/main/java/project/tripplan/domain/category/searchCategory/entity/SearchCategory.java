package project.tripplan.domain.category.searchCategory.entity;

import jakarta.persistence.*;
import lombok.*;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class SearchCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "self_search_place_id")
    private Long selfSearchPlaceId;

    private String name;

    private int depth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private SearchCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchCategory> children = new ArrayList<>();

}
