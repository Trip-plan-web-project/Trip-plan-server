package project.tripplan.domain.category.planCategory.enums;

import lombok.Getter;

@Getter
public enum PlanCategoryName {
    FOOD("food.png"),
    SIGHTSEEING("sightseeing.png"),
    ACCOMMODATION("accommodation.png"),
    TRANSPORTATION("transportation.png"),
    ENTERTAINMENT("entertainment.png");

    private final String image;

    PlanCategoryName(String image) {
        this.image = image;
    }
}