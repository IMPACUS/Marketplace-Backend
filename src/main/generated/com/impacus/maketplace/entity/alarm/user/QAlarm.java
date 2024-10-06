package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = -493839880L;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath email = createBoolean("email");

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

    public QAlarm(String variable) {
        super(Alarm.class, forVariable(variable));
    }

    public QAlarm(Path<? extends Alarm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarm(PathMetadata metadata) {
        super(Alarm.class, metadata);
    }

}

