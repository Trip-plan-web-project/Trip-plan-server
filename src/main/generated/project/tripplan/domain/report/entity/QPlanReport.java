package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanReport is a Querydsl query type for PlanReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanReport extends EntityPathBase<PlanReport> {

    private static final long serialVersionUID = -1633172851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanReport planReport = new QPlanReport("planReport");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.tripplan.domain.plan.entity.QPlan plan;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final project.tripplan.domain.user.entity.QUser user;

    public QPlanReport(String variable) {
        this(PlanReport.class, forVariable(variable), INITS);
    }

    public QPlanReport(Path<? extends PlanReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanReport(PathMetadata metadata, PathInits inits) {
        this(PlanReport.class, metadata, inits);
    }

    public QPlanReport(Class<? extends PlanReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new project.tripplan.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.user = inits.isInitialized("user") ? new project.tripplan.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

