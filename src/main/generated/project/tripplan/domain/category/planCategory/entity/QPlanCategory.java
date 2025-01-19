package project.tripplan.domain.category.planCategory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlanCategory is a Querydsl query type for PlanCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanCategory extends EntityPathBase<PlanCategory> {

    private static final long serialVersionUID = -2002994372L;

    public static final QPlanCategory planCategory = new QPlanCategory("planCategory");

    public final project.tripplan.global.common.entity.QBaseEntity _super = new project.tripplan.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlanCategory(String variable) {
        super(PlanCategory.class, forVariable(variable));
    }

    public QPlanCategory(Path<? extends PlanCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlanCategory(PathMetadata metadata) {
        super(PlanCategory.class, metadata);
    }

}

