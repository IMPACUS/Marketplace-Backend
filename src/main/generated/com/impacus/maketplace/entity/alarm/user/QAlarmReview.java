package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmReview is a Querydsl query type for AlarmReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmReview extends EntityPathBase<AlarmReview> {

    private static final long serialVersionUID = 548873072L;

    public static final QAlarmReview alarmReview = new QAlarmReview("alarmReview");

    public final QAlarm _super = new QAlarm(this);

    public final EnumPath<com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum> category = createEnum("category", com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum.class);

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

    public QAlarmReview(String variable) {
        super(AlarmReview.class, forVariable(variable));
    }

    public QAlarmReview(Path<? extends AlarmReview> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmReview(PathMetadata metadata) {
        super(AlarmReview.class, metadata);
    }

}

