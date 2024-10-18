package com.impacus.maketplace.entity.alarm.token;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBizgoToken is a Querydsl query type for BizgoToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBizgoToken extends EntityPathBase<AlarmToken> {

    private static final long serialVersionUID = -817348517L;

    public static final QBizgoToken bizgoToken = new QBizgoToken("bizgoToken");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> expiredDate = createDateTime("expiredDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath token = createString("token");

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmTokenEnum> type = createEnum("type", com.impacus.maketplace.common.enumType.alarm.AlarmTokenEnum.class);

    public QBizgoToken(String variable) {
        super(AlarmToken.class, forVariable(variable));
    }

    public QBizgoToken(Path<? extends AlarmToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBizgoToken(PathMetadata metadata) {
        super(AlarmToken.class, metadata);
    }

}

