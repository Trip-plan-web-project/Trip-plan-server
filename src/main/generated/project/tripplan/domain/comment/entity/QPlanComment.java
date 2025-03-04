package project.tripplan.domain.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanComment is a Querydsl query type for PlanComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanComment extends EntityPathBase<PlanComment> {

    private static final long serialVersionUID = -1680144007L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanComment planComment = new QPlanComment("planComment");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.plan.entity.QPlan plan;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public QPlanComment(String variable) {
        this(PlanComment.class, forVariable(variable), INITS);
    }

    public QPlanComment(Path<? extends PlanComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanComment(PathMetadata metadata, PathInits inits) {
        this(PlanComment.class, metadata, inits);
    }

    public QPlanComment(Class<? extends PlanComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new project.tripplan.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

