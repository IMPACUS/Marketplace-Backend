package com.impacus.maketplace.entity.point.greenLablePoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGreenLabelPoint is a Querydsl query type for GreenLabelPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGreenLabelPoint extends EntityPathBase<GreenLabelPoint> {

    private static final long serialVersionUID = 679527667L;

    public static final QGreenLabelPoint greenLabelPoint1 = new QGreenLabelPoint("greenLabelPoint1");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> greenLabelPoint = createNumber("greenLabelPoint", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QGreenLabelPoint(String variable) {
        super(GreenLabelPoint.class, forVariable(variable));
    }

    public QGreenLabelPoint(Path<? extends GreenLabelPoint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGreenLabelPoint(PathMetadata metadata) {
        super(GreenLabelPoint.class, metadata);
    }

}

