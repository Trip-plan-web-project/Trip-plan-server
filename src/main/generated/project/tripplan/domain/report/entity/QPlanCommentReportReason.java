package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanCommentReportReason is a Querydsl query type for PlanCommentReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanCommentReportReason extends EntityPathBase<PlanCommentReportReason> {

    private static final long serialVersionUID = 1903924190L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanCommentReportReason planCommentReportReason = new QPlanCommentReportReason("planCommentReportReason");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlanCommentReport planCommentReport;

    public final QReportReason reportReason;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanCommentReportReason(String variable) {
        this(PlanCommentReportReason.class, forVariable(variable), INITS);
    }

    public QPlanCommentReportReason(Path<? extends PlanCommentReportReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanCommentReportReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanCommentReportReason(PathMetadata metadata, PathInits inits) {
        this(PlanCommentReportReason.class, metadata, inits);
    }

    public QPlanCommentReportReason(Class<? extends PlanCommentReportReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planCommentReport = inits.isInitialized("planCommentReport") ? new QPlanCommentReport(forProperty("planCommentReport"), inits.get("planCommentReport")) : null;
        this.reportReason = inits.isInitialized("reportReason") ? new QReportReason(forProperty("reportReason")) : null;
    }

}

