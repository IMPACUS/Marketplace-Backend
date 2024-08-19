package com.impacus.maketplace.entity.userAlarm;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBrandShopAlarm is a Querydsl query type for BrandShopAlarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrandShopAlarm extends EntityPathBase<BrandShopAlarm> {

    private static final long serialVersionUID = 822945257L;

    public static final QBrandShopAlarm brandShopAlarm = new QBrandShopAlarm("brandShopAlarm");

    public final QAlarm _super = new QAlarm(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final BooleanPath email = _super.email;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath isAlarm = _super.isAlarm;

    //inherited
    public final BooleanPath kakao = _super.kakao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    //inherited
    public final BooleanPath sns = _super.sns;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> time = _super.time;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QBrandShopAlarm(String variable) {
        super(BrandShopAlarm.class, forVariable(variable));
    }

    public QBrandShopAlarm(Path<? extends BrandShopAlarm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBrandShopAlarm(PathMetadata metadata) {
        super(BrandShopAlarm.class, metadata);
    }

}

