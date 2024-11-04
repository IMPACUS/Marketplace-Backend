package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmUser is a Querydsl query type for AlarmUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmUser extends EntityPathBase<AlarmUser> {

    private static final long serialVersionUID = -1804911133L;

    public static final QAlarmUser alarmUser = new QAlarmUser("alarmUser");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum> category = createEnum("category", com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath email = createBoolean("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOn = createBoolean("isOn");

    public final BooleanPath kakao = createBoolean("kakao");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final BooleanPath msg = createBoolean("msg");

    public final BooleanPath push = createBoolean("push");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAlarmUser(String variable) {
        super(AlarmUser.class, forVariable(variable));
    }

    public QAlarmUser(Path<? extends AlarmUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmUser(PathMetadata metadata) {
        super(AlarmUser.class, metadata);
    }

}

