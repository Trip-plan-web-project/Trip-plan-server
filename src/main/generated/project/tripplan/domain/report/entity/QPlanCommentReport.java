package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanCommentReport is a Querydsl query type for PlanCommentReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanCommentReport extends EntityPathBase<PlanCommentReport> {

    private static final long serialVersionUID = 29702394L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanCommentReport planCommentReport = new QPlanCommentReport("planCommentReport");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final project.tripplan.domain.comment.entity.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public QPlanCommentReport(String variable) {
        this(PlanCommentReport.class, forVariable(variable), INITS);
    }

    public QPlanCommentReport(Path<? extends PlanCommentReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanCommentReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanCommentReport(PathMetadata metadata, PathInits inits) {
        this(PlanCommentReport.class, metadata, inits);
    }

    public QPlanCommentReport(Class<? extends PlanCommentReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new project.tripplan.domain.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

