package project.tripplan.domain.plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanSelfSearchCategory is a Querydsl query type for PlanSelfSearchCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanSelfSearchCategory extends EntityPathBase<PlanSelfSearchCategory> {

    private static final long serialVersionUID = -1465326592L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanSelfSearchCategory planSelfSearchCategory = new QPlanSelfSearchCategory("planSelfSearchCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlan plan;

    public final project.tripplan.domain.category.selfSearchCategory.entity.QSelfSearchCategory selfSearchCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanSelfSearchCategory(String variable) {
        this(PlanSelfSearchCategory.class, forVariable(variable), INITS);
    }

    public QPlanSelfSearchCategory(Path<? extends PlanSelfSearchCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanSelfSearchCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanSelfSearchCategory(PathMetadata metadata, PathInits inits) {
        this(PlanSelfSearchCategory.class, metadata, inits);
    }

    public QPlanSelfSearchCategory(Class<? extends PlanSelfSearchCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.selfSearchCategory = inits.isInitialized("selfSearchCategory") ? new project.tripplan.domain.category.selfSearchCategory.entity.QSelfSearchCategory(forProperty("selfSearchCategory"), inits.get("selfSearchCategory")) : null;
    }

}

