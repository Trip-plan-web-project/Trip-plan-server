package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewCommentReportReason is a Querydsl query type for ReviewCommentReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewCommentReportReason extends EntityPathBase<ReviewCommentReportReason> {

    private static final long serialVersionUID = -760842417L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewCommentReportReason reviewCommentReportReason = new QReviewCommentReportReason("reviewCommentReportReason");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReportReason reportReason;

    public final QReviewCommentReport reviewCommentReport;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewCommentReportReason(String variable) {
        this(ReviewCommentReportReason.class, forVariable(variable), INITS);
    }

    public QReviewCommentReportReason(Path<? extends ReviewCommentReportReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewCommentReportReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewCommentReportReason(PathMetadata metadata, PathInits inits) {
        this(ReviewCommentReportReason.class, metadata, inits);
    }

    public QReviewCommentReportReason(Class<? extends ReviewCommentReportReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportReason = inits.isInitialized("reportReason") ? new QReportReason(forProperty("reportReason")) : null;
        this.reviewCommentReport = inits.isInitialized("reviewCommentReport") ? new QReviewCommentReport(forProperty("reviewCommentReport"), inits.get("reviewCommentReport")) : null;
    }

}

