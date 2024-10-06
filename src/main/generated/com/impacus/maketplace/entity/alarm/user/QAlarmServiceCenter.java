package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmServiceCenter is a Querydsl query type for AlarmServiceCenter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmServiceCenter extends EntityPathBase<AlarmServiceCenter> {

    private static final long serialVersionUID = -1138660270L;

    public static final QAlarmServiceCenter alarmServiceCenter = new QAlarmServiceCenter("alarmServiceCenter");

    public final QAlarm _super = new QAlarm(this);

    public final EnumPath<com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum> category = createEnum("category", com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum.class);

    //inherited
    public final StringPath comment = _super.comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final BooleanPath email = _super.email;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath kakao = _super.kakao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final BooleanPath msg = _super.msg;

    //inherited
    public final BooleanPath push = _super.push;

    //inherited
    public final StringPath registerId = _super.registerId;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QAlarmServiceCenter(String variable) {
        super(AlarmServiceCenter.class, forVariable(variable));
    }

    public QAlarmServiceCenter(Path<? extends AlarmServiceCenter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmServiceCenter(PathMetadata metadata) {
        super(AlarmServiceCenter.class, metadata);
    }

}

