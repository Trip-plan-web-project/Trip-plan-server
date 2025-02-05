package project.tripplan.domain.plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = -1354575314L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlan plan = new QPlan("plan");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final ListPath<project.tripplan.domain.comment.entity.Comment, project.tripplan.domain.comment.entity.QComment> comments = this.<project.tripplan.domain.comment.entity.Comment, project.tripplan.domain.comment.entity.QComment>createList("comments", project.tripplan.domain.comment.entity.Comment.class, project.tripplan.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> people = createNumber("people", Integer.class);

    public final SetPath<project.tripplan.domain.planDay.entity.PlanDay, project.tripplan.domain.planDay.entity.QPlanDay> planDays = this.<project.tripplan.domain.planDay.entity.PlanDay, project.tripplan.domain.planDay.entity.QPlanDay>createSet("planDays", project.tripplan.domain.planDay.entity.PlanDay.class, project.tripplan.domain.planDay.entity.QPlanDay.class, PathInits.DIRECT2);

    public final ListPath<project.tripplan.domain.planLike.entity.PlanLike, project.tripplan.domain.planLike.entity.QPlanLike> planLikes = this.<project.tripplan.domain.planLike.entity.PlanLike, project.tripplan.domain.planLike.entity.QPlanLike>createList("planLikes", project.tripplan.domain.planLike.entity.PlanLike.class, project.tripplan.domain.planLike.entity.QPlanLike.class, PathInits.DIRECT2);

    public final SetPath<PlanPlaceCategory, QPlanPlaceCategory> planPlaceCategories = this.<PlanPlaceCategory, QPlanPlaceCategory>createSet("planPlaceCategories", PlanPlaceCategory.class, QPlanPlaceCategory.class, PathInits.DIRECT2);

    public final SetPath<PlanTransportationCategory, QPlanTransportationCategory> planTransportationCategories = this.<PlanTransportationCategory, QPlanTransportationCategory>createSet("planTransportationCategories", PlanTransportationCategory.class, QPlanTransportationCategory.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<project.tripplan.domain.plan.enums.PlanStatus> status = createEnum("status", project.tripplan.domain.plan.enums.PlanStatus.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> totalCost = createNumber("totalCost", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPlan(String variable) {
        this(Plan.class, forVariable(variable), INITS);
    }

    public QPlan(Path<? extends Plan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlan(PathMetadata metadata, PathInits inits) {
        this(Plan.class, metadata, inits);
    }

    public QPlan(Class<? extends Plan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

