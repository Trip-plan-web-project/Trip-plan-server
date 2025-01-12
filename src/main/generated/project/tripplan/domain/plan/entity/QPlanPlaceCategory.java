package project.tripplan.domain.plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanPlaceCategory is a Querydsl query type for PlanPlaceCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanPlaceCategory extends EntityPathBase<PlanPlaceCategory> {

    private static final long serialVersionUID = 1644944279L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanPlaceCategory planPlaceCategory = new QPlanPlaceCategory("planPlaceCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.category.placeCategory.entity.QPlaceCategory placeCategory;

    public final QPlan plan;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanPlaceCategory(String variable) {
        this(PlanPlaceCategory.class, forVariable(variable), INITS);
    }

    public QPlanPlaceCategory(Path<? extends PlanPlaceCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanPlaceCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanPlaceCategory(PathMetadata metadata, PathInits inits) {
        this(PlanPlaceCategory.class, metadata, inits);
    }

    public QPlanPlaceCategory(Class<? extends PlanPlaceCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.placeCategory = inits.isInitialized("placeCategory") ? new project.tripplan.domain.category.placeCategory.entity.QPlaceCategory(forProperty("placeCategory"), inits.get("placeCategory")) : null;
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan"), inits.get("plan")) : null;
    }

}

