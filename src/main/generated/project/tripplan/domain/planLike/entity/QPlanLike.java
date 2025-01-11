package project.tripplan.domain.planLike.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanLike is a Querydsl query type for PlanLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanLike extends EntityPathBase<PlanLike> {

    private static final long serialVersionUID = 1541632156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanLike planLike = new QPlanLike("planLike");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.plan.entity.QPlan plan;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public QPlanLike(String variable) {
        this(PlanLike.class, forVariable(variable), INITS);
    }

    public QPlanLike(Path<? extends PlanLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanLike(PathMetadata metadata, PathInits inits) {
        this(PlanLike.class, metadata, inits);
    }

    public QPlanLike(Class<? extends PlanLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new project.tripplan.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

