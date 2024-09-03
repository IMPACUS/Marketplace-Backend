package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -1249327551L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.AutoManualType> autoManualType = createEnum("autoManualType", com.impacus.maketplace.common.enumType.coupon.AutoManualType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType = createEnum("benefitType", com.impacus.maketplace.common.enumType.coupon.BenefitType.class);

    public final NumberPath<Long> benefitValue = createNumber("benefitValue", Long.class);

    public final StringPath code = createString("code");

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CouponIssueType> couponIssueType = createEnum("couponIssueType", com.impacus.maketplace.common.enumType.coupon.CouponIssueType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CouponType> couponType = createEnum("couponType", com.impacus.maketplace.common.enumType.coupon.CouponType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath description = createString("description");

    public final BooleanPath emailAlarm = createBoolean("emailAlarm");

    public final NumberPath<Integer> expireTimeDays = createNumber("expireTimeDays", Integer.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.ExpireTimeType> expireTimeType = createEnum("expireTimeType", com.impacus.maketplace.common.enumType.coupon.ExpireTimeType.class);

    public final NumberPath<Integer> firstCount = createNumber("firstCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.StandardType> issueConditionType = createEnum("issueConditionType", com.impacus.maketplace.common.enumType.coupon.StandardType.class);

    public final NumberPath<Long> issueConditionValue = createNumber("issueConditionValue", Long.class);

    public final StringPath issueCoverageSubCategoryName = createString("issueCoverageSubCategoryName");

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CoverageType> issueCoverageType = createEnum("issueCoverageType", com.impacus.maketplace.common.enumType.coupon.CoverageType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.IssuedTimeType> issuedTimeType = createEnum("issuedTimeType", com.impacus.maketplace.common.enumType.coupon.IssuedTimeType.class);

    public final BooleanPath kakaoAlarm = createBoolean("kakaoAlarm");

    public final BooleanPath loginAlarm = createBoolean("loginAlarm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final NumberPath<Long> numberOfPeriod = createNumber("numberOfPeriod", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.PaymentTarget> paymentTarget = createEnum("paymentTarget", com.impacus.maketplace.common.enumType.coupon.PaymentTarget.class);

    public final DatePath<java.time.LocalDate> periodEndAt = createDate("periodEndAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> periodStartAt = createDate("periodStartAt", java.time.LocalDate.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.PeriodType> periodType = createEnum("periodType", com.impacus.maketplace.common.enumType.coupon.PeriodType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.ProductType> productType = createEnum("productType", com.impacus.maketplace.common.enumType.coupon.ProductType.class);

    public final NumberPath<Long> quantityIssued = createNumber("quantityIssued", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final BooleanPath smsAlarm = createBoolean("smsAlarm");

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CouponStatusType> statusType = createEnum("statusType", com.impacus.maketplace.common.enumType.coupon.CouponStatusType.class);

    public final StringPath useCoverageSubCategoryName = createString("useCoverageSubCategoryName");

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.CoverageType> useCoverageType = createEnum("useCoverageType", com.impacus.maketplace.common.enumType.coupon.CoverageType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.StandardType> useStandardType = createEnum("useStandardType", com.impacus.maketplace.common.enumType.coupon.StandardType.class);

    public final NumberPath<Long> useStandardValue = createNumber("useStandardValue", Long.class);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

