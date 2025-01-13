package project.tripplan.domain.planDayDetail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanDayDetail is a Querydsl query type for PlanDayDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanDayDetail extends EntityPathBase<PlanDayDetail> {

    private static final long serialVersionUID = 1754812930L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanDayDetail planDayDetail = new QPlanDayDetail("planDayDetail");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> orderIndex = createNumber("orderIndex", Integer.class);

    public final StringPath placeName = createString("placeName");

    public final StringPath planCategoryName = createString("planCategoryName");

    public final project.tripplan.domain.planDay.entity.QPlanDay planDay;

    public final StringPath streetAddress = createString("streetAddress");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanDayDetail(String variable) {
        this(PlanDayDetail.class, forVariable(variable), INITS);
    }

    public QPlanDayDetail(Path<? extends PlanDayDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanDayDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanDayDetail(PathMetadata metadata, PathInits inits) {
        this(PlanDayDetail.class, metadata, inits);
    }

    public QPlanDayDetail(Class<? extends PlanDayDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planDay = inits.isInitialized("planDay") ? new project.tripplan.domain.planDay.entity.QPlanDay(forProperty("planDay"), inits.get("planDay")) : null;
    }

}

