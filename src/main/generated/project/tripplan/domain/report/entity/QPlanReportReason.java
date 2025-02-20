package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanReportReason is a Querydsl query type for PlanReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanReportReason extends EntityPathBase<PlanReportReason> {

    private static final long serialVersionUID = -221813967L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanReportReason planReportReason = new QPlanReportReason("planReportReason");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlanReport planReport;

    public final QReportReason reportReason;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanReportReason(String variable) {
        this(PlanReportReason.class, forVariable(variable), INITS);
    }

    public QPlanReportReason(Path<? extends PlanReportReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanReportReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanReportReason(PathMetadata metadata, PathInits inits) {
        this(PlanReportReason.class, metadata, inits);
    }

    public QPlanReportReason(Class<? extends PlanReportReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planReport = inits.isInitialized("planReport") ? new QPlanReport(forProperty("planReport"), inits.get("planReport")) : null;
        this.reportReason = inits.isInitialized("reportReason") ? new QReportReason(forProperty("reportReason")) : null;
    }

}

