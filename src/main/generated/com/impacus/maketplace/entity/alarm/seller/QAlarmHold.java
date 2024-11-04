package com.impacus.maketplace.entity.alarm.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmHold is a Querydsl query type for AlarmHold
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmHold extends EntityPathBase<AlarmHold> {

    private static final long serialVersionUID = -888105205L;

    public static final QAlarmHold alarmHold = new QAlarmHold("alarmHold");

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final BooleanPath email = createBoolean("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath kakao = createBoolean("kakao");

    public final StringPath kakaoCode = createString("kakaoCode");

    public final BooleanPath msg = createBoolean("msg");

    public final StringPath phone = createString("phone");

    public final StringPath receiver = createString("receiver");

    public final StringPath registerId = createString("registerId");

    public final TimePath<java.time.LocalTime> sendTime = createTime("sendTime", java.time.LocalTime.class);

    public final StringPath subject = createString("subject");

    public final StringPath text = createString("text");

    public QAlarmHold(String variable) {
        super(AlarmHold.class, forVariable(variable));
    }

    public QAlarmHold(Path<? extends AlarmHold> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmHold(PathMetadata metadata) {
        super(AlarmHold.class, metadata);
    }

}

