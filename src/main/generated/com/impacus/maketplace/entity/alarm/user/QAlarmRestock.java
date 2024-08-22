package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmRestock is a Querydsl query type for AlarmRestock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmRestock extends EntityPathBase<AlarmRestock> {

    private static final long serialVersionUID = -167237717L;

    public static final QAlarmRestock alarmRestock = new QAlarmRestock("alarmRestock");

    public final QAlarm _super = new QAlarm(this);

    //inherited
    public final StringPath comment1 = _super.comment1;

    //inherited
    public final StringPath comment2 = _super.comment2;

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
    public final DateTimePath<java.time.LocalDateTime> time = _super.time;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QAlarmRestock(String variable) {
        super(AlarmRestock.class, forVariable(variable));
    }

    public QAlarmRestock(Path<? extends AlarmRestock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmRestock(PathMetadata metadata) {
        super(AlarmRestock.class, metadata);
    }

}

