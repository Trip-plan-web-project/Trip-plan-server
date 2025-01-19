package project.tripplan.domain.category.placeCategory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceCategory is a Querydsl query type for PlaceCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceCategory extends EntityPathBase<PlaceCategory> {

    private static final long serialVersionUID = 386056180L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceCategory placeCategory = new QPlaceCategory("placeCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final ListPath<PlaceCategory, QPlaceCategory> children = this.<PlaceCategory, QPlaceCategory>createList("children", PlaceCategory.class, QPlaceCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QPlaceCategory parent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlaceCategory(String variable) {
        this(PlaceCategory.class, forVariable(variable), INITS);
    }

    public QPlaceCategory(Path<? extends PlaceCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceCategory(PathMetadata metadata, PathInits inits) {
        this(PlaceCategory.class, metadata, inits);
    }

    public QPlaceCategory(Class<? extends PlaceCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QPlaceCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

