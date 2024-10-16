package com.impacus.maketplace.entity.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseInfo is a Querydsl query type for BaseInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBaseInfo extends EntityPathBase<BaseInfo> {

    private static final long serialVersionUID = -1699430827L;

    public static final QBaseInfo baseInfo = new QBaseInfo("baseInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath detail = createString("detail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.common.InfoType> infoType = createEnum("infoType", com.impacus.maketplace.common.enumType.common.InfoType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath title = createString("title");

    public QBaseInfo(String variable) {
        super(BaseInfo.class, forVariable(variable));
    }

    public QBaseInfo(Path<? extends BaseInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseInfo(PathMetadata metadata) {
        super(BaseInfo.class, metadata);
    }

}

