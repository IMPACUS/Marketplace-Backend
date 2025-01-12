package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.api.QAlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.QCouponNameDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponHistoryDTO;
import com.impacus.maketplace.dto.coupon.response.QIssueCouponHistoryDTO;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.impacus.maketplace.entity.coupon.QUserCoupon;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponApiRepositoryImpl implements CouponApiRepository {

    private final JPAQueryFactory queryFactory;

    private final QCoupon coupon = QCoupon.coupon;
    private final QUserCoupon userCoupon = QUserCoupon.userCoupon;
    private final QUser user = QUser.user;

    @Override
    public List<CouponNameDTO> getCouponNames() {
        return queryFactory
                .select(new QCouponNameDTO(
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
        return queryFactory
                .select(new QAlarmCouponDTO(
                        userCoupon.userId,
                        user.name,
                        user.phoneNumberPrefix,
                        user.phoneNumberSuffix,
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

    @Override
    public List<IssueCouponHistoryDTO> findIssueCouponHistories(IdsDTO dto) {
        return queryFactory
                .select(new QIssueCouponHistoryDTO(
                        userCoupon.id,
                        coupon.code,
                        coupon.description,
                        coupon.name,
                        user.email,
                        user.name,
                        userCoupon.status,
                        userCoupon.createAt,
                        coupon.benefitType,
                        coupon.benefitValue
                ))
                .from(userCoupon)
                .join(coupon).on(coupon.id.eq(userCoupon.couponId))
                .join(user).on(user.id.eq(userCoupon.userId))
                .where(userCoupon.id.in(dto.getIds()))
                .fetch();
    }

    private BooleanExpression betweenDate(LocalDate startAt, LocalDate endAt) {
        if (startAt != null && endAt != null) {
            LocalDateTime startDateTime = startAt.atStartOfDay(); // 시작 날짜의 00:00
            LocalDateTime endDateTime = endAt.atTime(23, 59, 59, 999999999); // 종료 날짜의 23:59:59.999999999
            return userCoupon.createAt.between(startDateTime, endDateTime);
        } else if (startAt != null) {
            LocalDateTime startDateTime = startAt.atStartOfDay();
            return userCoupon.createAt.goe(startDateTime);
        } else if (endAt != null) {
            LocalDateTime endDateTime = endAt.atTime(23, 59, 59, 999999999);
            return userCoupon.createAt.loe(endDateTime);
        }
        return null;
    }
    private BooleanExpression userCouponStatusEq(UserCouponStatus userCouponStatus) {
        return userCouponStatus != null ? userCoupon.status.eq(userCouponStatus) : null;
    }

    private BooleanExpression couponNameEq(String name) {
        return StringUtils.hasText(name) ? coupon.name.contains(name) : null;
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
