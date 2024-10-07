package com.impacus.maketplace.entity.alarm.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmWish is a Querydsl query type for AlarmWish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmWish extends EntityPathBase<AlarmWish> {

    private static final long serialVersionUID = -887663885L;

    public static final QAlarmWish alarmWish = new QAlarmWish("alarmWish");

    public final QAlarm _super = new QAlarm(this);

    public final EnumPath<com.impacus.maketplace.entity.alarm.seller.enums.WishEnum> category = createEnum("category", com.impacus.maketplace.entity.alarm.seller.enums.WishEnum.class);

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

    public QAlarmWish(String variable) {
        super(AlarmWish.class, forVariable(variable));
    }

    public QAlarmWish(Path<? extends AlarmWish> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmWish(PathMetadata metadata) {
        super(AlarmWish.class, metadata);
    }

}

