package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.dto.coupon.response.*;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.impacus.maketplace.entity.coupon.QUserCoupon;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CouponCustomRepositoryImpl implements CouponCustomRepositroy {

    private final JPAQueryFactory queryFactory;

    private final QCoupon coupon = QCoupon.coupon;
    private final QUserCoupon userCoupon = QUserCoupon.userCoupon;
    private final QUser user = QUser.user;

    @Override
    public Page<CouponListInfoDTO> findCouponListInfo(String name, CouponStatusType couponStatus, Pageable pageable) {
        // Load Data
        List<CouponListInfoDTO> content = queryFactory
                .select(new QCouponListInfoDTO(
                        coupon.id,
                        coupon.code,
                        coupon.name,
                        coupon.issueConditionType,
                        coupon.issueConditionValue,
                        coupon.expireTimeType,
                        coupon.expireTimeDays,
                        coupon.quantityIssued,
                        coupon.autoManualType,
                        coupon.statusType,
                        coupon.modifyAt))
                .from(coupon)
                .where(couponNameEq(name),
                        couponStatusEq(couponStatus),
                        coupon.isDeleted.eq(false))
                .offset(pageable.getOffset())   // 추후 기능 개선: NoOffset으로 성능 방식 고려
                .limit(pageable.getPageSize())
                .fetch();

        // Count Data
        JPAQuery<Long> countQuery = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(couponNameEq(name),
                        couponStatusEq(couponStatus),
                        coupon.isDeleted.eq(false));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<IssueCouponInfoDTO> findIssueCouponInfoList() {
        return queryFactory
                .select(new QIssueCouponInfoDTO(
                        coupon.id,
                        coupon.name,
                        coupon.benefitType,
                        coupon.benefitValue,
                        coupon.description,
                        coupon.issuedTimeType,
                        coupon.expireTimeType,
                        coupon.expireTimeDays,
                        coupon.useStandardType,
                        coupon.useStandardValue,
                        coupon.useCoverageType,
                        coupon.useCoverageSubCategoryName,
                        coupon.smsAlarm,
                        coupon.emailAlarm,
                        coupon.kakaoAlarm
                ))
                .from(coupon)
                .where(checkCanIssueCoupon())
                .fetch();
    }

    @Override
    public Page<IssueCouponHIstoryDTO> findIssueCouponHistoryList(String name, UserCouponStatus userCouponStatus, LocalDate startAt, LocalDate endAt, Pageable pageable) {
        List<IssueCouponHIstoryDTO> content = queryFactory
                .select(new QIssueCouponHIstoryDTO(
                        userCoupon.couponId,
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
                .where(
                        couponNameEq(name),
                        userCouponStatusEq(userCouponStatus),
                        betweenDate(startAt, endAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Data
        JPAQuery<Long> countQuery = queryFactory
                .select(coupon.count())
                .from(coupon)
                .join(coupon).on(coupon.id.eq(userCoupon.couponId))
                .where(
                        couponNameEq(name),
                        userCouponStatusEq(userCouponStatus),
                        betweenDate(startAt, endAt));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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

    private BooleanExpression checkCanIssueCoupon() {
        return (coupon.statusType.eq(CouponStatusType.ISSUED).or(coupon.statusType.eq(CouponStatusType.ISSUING))).and(couponIsNotDeleted());
    }

    private BooleanExpression couponStatusEq(CouponStatusType couponStatus) {
        return couponStatus != null ? coupon.statusType.eq(couponStatus) : null;
    }

    private BooleanExpression couponNameEq(String name) {
        return StringUtils.hasText(name) ? coupon.name.contains(name) : null;
    }

    private BooleanExpression couponIsNotDeleted() {
        return coupon.isDeleted.eq(false);
    }
}
