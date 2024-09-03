package com.impacus.maketplace.entity.point.greenLablePoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGreenLabelPointHistoryRelation is a Querydsl query type for GreenLabelPointHistoryRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGreenLabelPointHistoryRelation extends EntityPathBase<GreenLabelPointHistoryRelation> {

    private static final long serialVersionUID = 1826716989L;

    public static final QGreenLabelPointHistoryRelation greenLabelPointHistoryRelation = new QGreenLabelPointHistoryRelation("greenLabelPointHistoryRelation");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> greenLabelPointAllocationId = createNumber("greenLabelPointAllocationId", Long.class);

    public final NumberPath<Long> greenLabelPointHistoryId = createNumber("greenLabelPointHistoryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final DateTimePath<java.time.LocalDateTime> previousExpiredAt = createDateTime("previousExpiredAt", java.time.LocalDateTime.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public QGreenLabelPointHistoryRelation(String variable) {
        super(GreenLabelPointHistoryRelation.class, forVariable(variable));
    }

    public QGreenLabelPointHistoryRelation(Path<? extends GreenLabelPointHistoryRelation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGreenLabelPointHistoryRelation(PathMetadata metadata) {
        super(GreenLabelPointHistoryRelation.class, metadata);
    }

}

