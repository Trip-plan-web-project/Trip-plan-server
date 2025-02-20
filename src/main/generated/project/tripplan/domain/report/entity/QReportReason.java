package project.tripplan.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportReason is a Querydsl query type for ReportReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportReason extends EntityPathBase<ReportReason> {

    private static final long serialVersionUID = 1465049448L;

    public static final QReportReason reportReason1 = new QReportReason("reportReason1");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<project.tripplan.domain.report.enums.ReportReasons> reportReason = createEnum("reportReason", project.tripplan.domain.report.enums.ReportReasons.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReportReason(String variable) {
        super(ReportReason.class, forVariable(variable));
    }

    public QReportReason(Path<? extends ReportReason> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportReason(PathMetadata metadata) {
        super(ReportReason.class, metadata);
    }

}

