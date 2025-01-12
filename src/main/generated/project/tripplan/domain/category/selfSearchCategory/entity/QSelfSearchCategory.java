package project.tripplan.domain.category.selfSearchCategory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSelfSearchCategory is a Querydsl query type for SelfSearchCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelfSearchCategory extends EntityPathBase<SelfSearchCategory> {

    private static final long serialVersionUID = -1184505134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSelfSearchCategory selfSearchCategory = new QSelfSearchCategory("selfSearchCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    public final ListPath<SelfSearchCategory, QSelfSearchCategory> children = this.<SelfSearchCategory, QSelfSearchCategory>createList("children", SelfSearchCategory.class, QSelfSearchCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QSelfSearchCategory parent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSelfSearchCategory(String variable) {
        this(SelfSearchCategory.class, forVariable(variable), INITS);
    }

    public QSelfSearchCategory(Path<? extends SelfSearchCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSelfSearchCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSelfSearchCategory(PathMetadata metadata, PathInits inits) {
        this(SelfSearchCategory.class, metadata, inits);
    }

    public QSelfSearchCategory(Class<? extends SelfSearchCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QSelfSearchCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

