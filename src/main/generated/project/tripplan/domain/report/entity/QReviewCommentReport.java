package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewCommentReport is a Querydsl query type for ReviewCommentReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewCommentReport extends EntityPathBase<ReviewCommentReport> {

    private static final long serialVersionUID = 1207420203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewCommentReport reviewCommentReport = new QReviewCommentReport("reviewCommentReport");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.comment.entity.QReviewComment reviewComment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public QReviewCommentReport(String variable) {
        this(ReviewCommentReport.class, forVariable(variable), INITS);
    }

    public QReviewCommentReport(Path<? extends ReviewCommentReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewCommentReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewCommentReport(PathMetadata metadata, PathInits inits) {
        this(ReviewCommentReport.class, metadata, inits);
    }

    public QReviewCommentReport(Class<? extends ReviewCommentReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewComment = inits.isInitialized("reviewComment") ? new project.tripplan.domain.comment.entity.QReviewComment(forProperty("reviewComment"), inits.get("reviewComment")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

