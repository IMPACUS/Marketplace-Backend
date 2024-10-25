package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderGreenLabelPointHistory is a Querydsl query type for OrderGreenLabelPointHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderGreenLabelPointHistory extends EntityPathBase<OrderGreenLabelPointHistory> {

    private static final long serialVersionUID = -11949674L;

    public static final QOrderGreenLabelPointHistory orderGreenLabelPointHistory = new QOrderGreenLabelPointHistory("orderGreenLabelPointHistory");

    public final QGreenLabelPointHistory _super = new QGreenLabelPointHistory(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final NumberPath<Long> greenLabelPoint = _super.greenLabelPoint;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> levelPoint = _super.levelPoint;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath orderId = createString("orderId");

    //inherited
    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointStatus> pointStatus = _super.pointStatus;

    //inherited
    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointType> pointType = _super.pointType;

    //inherited
    public final StringPath registerId = _super.registerId;

    //inherited
    public final NumberPath<Long> tradeAmount = _super.tradeAmount;

    //inherited
    public final NumberPath<Long> unappliedPoint = _super.unappliedPoint;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QOrderGreenLabelPointHistory(String variable) {
        super(OrderGreenLabelPointHistory.class, forVariable(variable));
    }

    public QOrderGreenLabelPointHistory(Path<? extends OrderGreenLabelPointHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderGreenLabelPointHistory(PathMetadata metadata) {
        super(OrderGreenLabelPointHistory.class, metadata);
    }

}

