package com.impacus.maketplace.entity.alarm.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmOrder is a Querydsl query type for AlarmOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmOrder extends EntityPathBase<AlarmOrder> {

    private static final long serialVersionUID = -1754911102L;

    public static final QAlarmOrder alarmOrder = new QAlarmOrder("alarmOrder");

    public final QAlarm _super = new QAlarm(this);

    public final EnumPath<com.impacus.maketplace.entity.alarm.seller.enums.OrderEnum> category = createEnum("category", com.impacus.maketplace.entity.alarm.seller.enums.OrderEnum.class);

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

    public final EnumPath<com.impacus.maketplace.entity.alarm.seller.enums.OrderSubEnum> subcategory = createEnum("subcategory", com.impacus.maketplace.entity.alarm.seller.enums.OrderSubEnum.class);

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QAlarmOrder(String variable) {
        super(AlarmOrder.class, forVariable(variable));
    }

    public QAlarmOrder(Path<? extends AlarmOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmOrder(PathMetadata metadata) {
        super(AlarmOrder.class, metadata);
    }

}

