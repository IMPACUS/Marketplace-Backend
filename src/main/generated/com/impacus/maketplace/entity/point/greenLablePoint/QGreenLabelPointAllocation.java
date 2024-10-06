package com.impacus.maketplace.entity.point.greenLablePoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGreenLabelPointAllocation is a Querydsl query type for GreenLabelPointAllocation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGreenLabelPointAllocation extends EntityPathBase<GreenLabelPointAllocation> {

    private static final long serialVersionUID = -79323597L;

    public static final QGreenLabelPointAllocation greenLabelPointAllocation = new QGreenLabelPointAllocation("greenLabelPointAllocation");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Long> allocatedPoint = createNumber("allocatedPoint", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointUsageStatus> pointStatus = createEnum("pointStatus", com.impacus.maketplace.common.enumType.point.PointUsageStatus.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointType> pointType = createEnum("pointType", com.impacus.maketplace.common.enumType.point.PointType.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> remainPoint = createNumber("remainPoint", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QGreenLabelPointAllocation(String variable) {
        super(GreenLabelPointAllocation.class, forVariable(variable));
    }

    public QGreenLabelPointAllocation(Path<? extends GreenLabelPointAllocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGreenLabelPointAllocation(PathMetadata metadata) {
        super(GreenLabelPointAllocation.class, metadata);
    }

}

