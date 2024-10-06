package com.impacus.maketplace.entity.point.greenLablePoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGreenLabelPointHistory is a Querydsl query type for GreenLabelPointHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGreenLabelPointHistory extends EntityPathBase<GreenLabelPointHistory> {

    private static final long serialVersionUID = -485437407L;

    public static final QGreenLabelPointHistory greenLabelPointHistory = new QGreenLabelPointHistory("greenLabelPointHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointStatus> pointStatus = createEnum("pointStatus", com.impacus.maketplace.common.enumType.point.PointStatus.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointType> pointType = createEnum("pointType", com.impacus.maketplace.common.enumType.point.PointType.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> tradeAmount = createNumber("tradeAmount", Long.class);

    public final NumberPath<Long> unappliedPoint = createNumber("unappliedPoint", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QGreenLabelPointHistory(String variable) {
        super(GreenLabelPointHistory.class, forVariable(variable));
    }

    public QGreenLabelPointHistory(Path<? extends GreenLabelPointHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGreenLabelPointHistory(PathMetadata metadata) {
        super(GreenLabelPointHistory.class, metadata);
    }

}

