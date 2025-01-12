package project.tripplan.domain.planDay.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanDay is a Querydsl query type for PlanDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanDay extends EntityPathBase<PlanDay> {

    private static final long serialVersionUID = -1688005662L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanDay planDay = new QPlanDay("planDay");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final NumberPath<Integer> cost = createNumber("cost", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Integer> day = createNumber("day", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.plan.entity.QPlan plan;

    public final ListPath<project.tripplan.domain.planDayDetail.entity.PlanDayDetail, project.tripplan.domain.planDayDetail.entity.QPlanDayDetail> planDayDetails = this.<project.tripplan.domain.planDayDetail.entity.PlanDayDetail, project.tripplan.domain.planDayDetail.entity.QPlanDayDetail>createList("planDayDetails", project.tripplan.domain.planDayDetail.entity.PlanDayDetail.class, project.tripplan.domain.planDayDetail.entity.QPlanDayDetail.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanDay(String variable) {
        this(PlanDay.class, forVariable(variable), INITS);
    }

    public QPlanDay(Path<? extends PlanDay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanDay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanDay(PathMetadata metadata, PathInits inits) {
        this(PlanDay.class, metadata, inits);
    }

    public QPlanDay(Class<? extends PlanDay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new project.tripplan.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
    }

}

