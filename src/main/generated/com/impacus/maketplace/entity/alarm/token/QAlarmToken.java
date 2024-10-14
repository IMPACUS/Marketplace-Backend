package com.impacus.maketplace.entity.alarm.token;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmToken is a Querydsl query type for AlarmToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmToken extends EntityPathBase<AlarmToken> {

    private static final long serialVersionUID = -1946605177L;

    public static final QAlarmToken alarmToken = new QAlarmToken("alarmToken");

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

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAlarmToken(String variable) {
        super(AlarmToken.class, forVariable(variable));
    }

    public QAlarmToken(Path<? extends AlarmToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmToken(PathMetadata metadata) {
        super(AlarmToken.class, metadata);
    }

}

