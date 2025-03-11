package project.tripplan.domain.bookmark.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewBookmark is a Querydsl query type for ReviewBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewBookmark extends EntityPathBase<ReviewBookmark> {

    private static final long serialVersionUID = 1780832320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewBookmark reviewBookmark = new QReviewBookmark("reviewBookmark");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.review.entity.QReview review;

    public final project.tripplan.domain.user.entity.QUser user;

    public QReviewBookmark(String variable) {
        this(ReviewBookmark.class, forVariable(variable), INITS);
    }

    public QReviewBookmark(Path<? extends ReviewBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewBookmark(PathMetadata metadata, PathInits inits) {
        this(ReviewBookmark.class, metadata, inits);
    }

    public QReviewBookmark(Class<? extends ReviewBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new project.tripplan.domain.review.entity.QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

