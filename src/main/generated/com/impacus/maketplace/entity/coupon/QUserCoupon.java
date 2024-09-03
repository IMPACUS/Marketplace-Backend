package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserCoupon is a Querydsl query type for UserCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCoupon extends EntityPathBase<UserCoupon> {

    private static final long serialVersionUID = -1167141780L;

    public static final QUserCoupon userCoupon = new QUserCoupon("userCoupon");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> availableDownloadAt = createDate("availableDownloadAt", java.time.LocalDate.class);

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> downloadAt = createDateTime("downloadAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> expiredAt = createDate("expiredAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDownload = createBoolean("isDownload");

    public final BooleanPath isUsed = createBoolean("isUsed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sourceId = createNumber("sourceId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CouponSourceType> sourceType = createEnum("sourceType", com.impacus.maketplace.common.enumType.coupon.CouponSourceType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.UserCouponStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.coupon.UserCouponStatus.class);

    public final DateTimePath<java.time.LocalDateTime> usedAt = createDateTime("usedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserCoupon(String variable) {
        super(UserCoupon.class, forVariable(variable));
    }

    public QUserCoupon(Path<? extends UserCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserCoupon(PathMetadata metadata) {
        super(UserCoupon.class, metadata);
    }

}

