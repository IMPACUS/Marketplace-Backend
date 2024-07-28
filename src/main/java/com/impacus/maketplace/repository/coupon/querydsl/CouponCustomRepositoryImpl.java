package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import com.impacus.maketplace.dto.coupon.response.PayCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.response.QCouponListInfoDTO;
import com.impacus.maketplace.dto.coupon.response.QPayCouponInfoDTO;
import com.impacus.maketplace.entity.coupon.QCoupon;
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

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CouponCustomRepositoryImpl implements CouponCustomRepositroy {

    private final JPAQueryFactory queryFactory;

    private final QCoupon coupon = QCoupon.coupon;

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
    public List<PayCouponInfoDTO> findPayCouponInfoList() {
        return queryFactory
                .select(new QPayCouponInfoDTO(
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
    private BooleanExpression checkCanIssueCoupon() {
        return (coupon.statusType.eq(CouponStatusType.ISSUED).or(coupon.statusType.eq(CouponStatusType.ISSUING))).and(couponIsNotDeleted());
    }

    private BooleanExpression couponStatusEq(CouponStatusType couponStatus) {
        log.info("CouponStatusType: " + couponStatus);
        return couponStatus != null ? coupon.statusType.eq(couponStatus) : null;
    }
    private BooleanExpression couponNameEq(String name) {
        return StringUtils.hasText(name) ? coupon.name.contains(name) : null;
    }
    private BooleanExpression couponIsNotDeleted() {
        return coupon.isDeleted.eq(false);
    }
}
