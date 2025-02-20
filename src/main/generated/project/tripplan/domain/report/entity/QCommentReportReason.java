package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentReportReason is a Querydsl query type for CommentReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentReportReason extends EntityPathBase<CommentReportReason> {

    private static final long serialVersionUID = 268892615L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentReportReason commentReportReason = new QCommentReportReason("commentReportReason");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final QCommentReport commentReport;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReportReason reportReason;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommentReportReason(String variable) {
        this(CommentReportReason.class, forVariable(variable), INITS);
    }

    public QCommentReportReason(Path<? extends CommentReportReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentReportReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentReportReason(PathMetadata metadata, PathInits inits) {
        this(CommentReportReason.class, metadata, inits);
    }

    public QCommentReportReason(Class<? extends CommentReportReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.commentReport = inits.isInitialized("commentReport") ? new QCommentReport(forProperty("commentReport"), inits.get("commentReport")) : null;
        this.reportReason = inits.isInitialized("reportReason") ? new QReportReason(forProperty("reportReason")) : null;
    }

}

