package com.impacus.maketplace.entity.alarm.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmBrandShop is a Querydsl query type for AlarmBrandShop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmBrandShop extends EntityPathBase<AlarmBrandShop> {

    private static final long serialVersionUID = -743064571L;

    public static final QAlarmBrandShop alarmBrandShop = new QAlarmBrandShop("alarmBrandShop");

    public final QAlarm _super = new QAlarm(this);

    public final EnumPath<com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum> category = createEnum("category", com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum.class);

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

    public QAlarmBrandShop(String variable) {
        super(AlarmBrandShop.class, forVariable(variable));
    }

    public QAlarmBrandShop(Path<? extends AlarmBrandShop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmBrandShop(PathMetadata metadata) {
        super(AlarmBrandShop.class, metadata);
    }

}

