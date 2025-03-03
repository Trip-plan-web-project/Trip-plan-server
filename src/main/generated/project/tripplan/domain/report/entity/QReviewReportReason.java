package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReportReason is a Querydsl query type for ReviewReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReportReason extends EntityPathBase<ReviewReportReason> {

    private static final long serialVersionUID = 1894386912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReportReason reviewReportReason = new QReviewReportReason("reviewReportReason");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReportReason reportReason;

    public final QReviewReport reviewReport;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewReportReason(String variable) {
        this(ReviewReportReason.class, forVariable(variable), INITS);
    }

    public QReviewReportReason(Path<? extends ReviewReportReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReportReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReportReason(PathMetadata metadata, PathInits inits) {
        this(ReviewReportReason.class, metadata, inits);
    }

    public QReviewReportReason(Class<? extends ReviewReportReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportReason = inits.isInitialized("reportReason") ? new QReportReason(forProperty("reportReason")) : null;
        this.reviewReport = inits.isInitialized("reviewReport") ? new QReviewReport(forProperty("reviewReport"), inits.get("reviewReport")) : null;
    }

}

