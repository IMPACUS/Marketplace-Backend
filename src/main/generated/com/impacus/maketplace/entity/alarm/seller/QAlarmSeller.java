package com.impacus.maketplace.entity.alarm.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmSeller is a Querydsl query type for AlarmSeller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmSeller extends EntityPathBase<AlarmSeller> {

    private static final long serialVersionUID = 1535086283L;

    public static final QAlarmSeller alarmSeller = new QAlarmSeller("alarmSeller");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum> category = createEnum("category", com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath email = createBoolean("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath kakao = createBoolean("kakao");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final BooleanPath msg = createBoolean("msg");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum> time = createEnum("time", com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAlarmSeller(String variable) {
        super(AlarmSeller.class, forVariable(variable));
    }

    public QAlarmSeller(Path<? extends AlarmSeller> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmSeller(PathMetadata metadata) {
        super(AlarmSeller.class, metadata);
    }

}

