package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.api.QAlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.QCouponNameDTO;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.impacus.maketplace.entity.coupon.QUserCoupon;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponApiRepositoryImpl implements CouponApiRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QCoupon coupon = QCoupon.coupon;
    private final QUserCoupon userCoupon = QUserCoupon.userCoupon;
    private final QUser user = QUser.user;

    @Override
    public List<CouponNameDTO> getCouponNames() {
        return jpaQueryFactory.select(new QCouponNameDTO(
                        coupon.id,
                        coupon.name,
                        coupon.benefitType,
                        coupon.benefitValue
                ))
                .from(coupon)
                .where(coupon.isDeleted.isFalse())
                .fetch();
    }

    @Override
    public List<AlarmCouponDTO> getAlarmCoupons() {
        return jpaQueryFactory.select(new QAlarmCouponDTO(
                        userCoupon.userId,
                        user.name,
                        user.phoneNumber,
                        user.email,
                        coupon.name,
                        coupon.benefitType,
                        coupon.benefitValue,
                        userCoupon.expiredAt
                ))
                .from(userCoupon)
                .join(coupon).on(coupon.id.eq(userCoupon.couponId))
                .join(user).on(user.id.eq(userCoupon.userId))
                .where(alarmCouponCond())
                .fetch();
    }

    private BooleanExpression alarmCouponCond() {
        return expiredCond()
                .and(userCoupon.isUsed.eq(false))
                .and(userCoupon.availableDownloadAt.goe(LocalDate.now()))
                .and(userCoupon.status.eq(UserCouponStatus.ISSUE_SUCCESS));
    }

    private BooleanExpression expiredCond() {
        return userCoupon.expiredAt.eq(LocalDate.now().plusDays(1)).or(
                userCoupon.expiredAt.eq(LocalDate.now().plusDays(30))
        );
    }
}
