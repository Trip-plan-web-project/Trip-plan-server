package project.tripplan.domain.category.transportationCategory.entitiy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransportationCategory is a Querydsl query type for TransportationCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransportationCategory extends EntityPathBase<TransportationCategory> {

    private static final long serialVersionUID = 1943972421L;

    public static final QTransportationCategory transportationCategory = new QTransportationCategory("transportationCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<project.tripplan.domain.category.transportationCategory.enums.TransportationName> name = createEnum("name", project.tripplan.domain.category.transportationCategory.enums.TransportationName.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransportationCategory(String variable) {
        super(TransportationCategory.class, forVariable(variable));
    }

    public QTransportationCategory(Path<? extends TransportationCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransportationCategory(PathMetadata metadata) {
        super(TransportationCategory.class, metadata);
    }

}

