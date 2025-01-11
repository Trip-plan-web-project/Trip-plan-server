package project.tripplan.domain.plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanTransportationCategory is a Querydsl query type for PlanTransportationCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanTransportationCategory extends EntityPathBase<PlanTransportationCategory> {

    private static final long serialVersionUID = 843436632L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanTransportationCategory planTransportationCategory = new QPlanTransportationCategory("planTransportationCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlan plan;

    public final project.tripplan.domain.category.transportationCategory.entitiy.QTransportationCategory transportationCategory;

    public QPlanTransportationCategory(String variable) {
        this(PlanTransportationCategory.class, forVariable(variable), INITS);
    }

    public QPlanTransportationCategory(Path<? extends PlanTransportationCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanTransportationCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanTransportationCategory(PathMetadata metadata, PathInits inits) {
        this(PlanTransportationCategory.class, metadata, inits);
    }

    public QPlanTransportationCategory(Class<? extends PlanTransportationCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.transportationCategory = inits.isInitialized("transportationCategory") ? new project.tripplan.domain.category.transportationCategory.entitiy.QTransportationCategory(forProperty("transportationCategory")) : null;
    }

}

