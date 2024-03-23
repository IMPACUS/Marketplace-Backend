package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.common.enumType.coupon.IssuanceStatus;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.dto.coupon.request.CouponSearchDto;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.dto.coupon.response.QCouponListDto;
import com.impacus.maketplace.dto.coupon.response.QCouponUserInfoResponse;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponCustomRepositoryImpl implements CouponCustomRepository{

    private final JPAQueryFactory queryFactory;

    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QUser userEntity = QUser.user;
    private final QCoupon couponEntity = QCoupon.coupon;

    @Override
    public CouponUserInfoResponse findByAddCouponInfo(CouponUserInfoRequest request) {

        CouponUserInfoResponse result = queryFactory.select(new QCouponUserInfoResponse(
                        userEntity.name,
                        Expressions.enumPath(UserStatus.class, userEntity.status.toString()),
                        pointMasterEntity.availablePoint,
                        pointMasterEntity.userScore,
                        Expressions.constant("010-000-0000"),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", userEntity.createAt),
                        Expressions.constant("profile-path"),
                        userEntity.email
                ))
                .from(pointMasterEntity)
                .innerJoin(userEntity).on(userEntity.id.eq(pointMasterEntity.userId))
                .where(userEntity.email.eq(request.getUserId()))
                .fetchOne();

        return result;
    }

    @Override
    public Page<CouponListDto> findAllCouponList(CouponSearchDto couponSearchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (couponSearchDto.getSearchCouponName() != null) {
            builder.and(couponEntity.name.like("%" + couponSearchDto.getSearchCouponName() + "%"));
        }
        if (StringUtils.isNotBlank(couponSearchDto.getOrderStatus())) {
            builder.and(couponEntity.status.eq(IssuanceStatus.fromCode(couponSearchDto.getOrderStatus().toLowerCase())));
        } else {
            builder.and(couponEntity.status.ne(IssuanceStatus.STOP));
        }

        JPAQuery<CouponListDto> query = queryFactory.select(new QCouponListDto(
                couponEntity.id,
                couponEntity.code,
                couponEntity.name,
                couponEntity.description,
                couponEntity.couponBenefitClassification,
                couponEntity.benefitAmount,
                couponEntity.couponIssuanceClassification,
                couponEntity.couponIssuanceClassificationData,
                couponEntity.couponPaymentTarget,
                couponEntity.firstComeFirstServedAmount,
                couponEntity.couponIssuedTime,
                couponEntity.couponExpireTime,
                couponEntity.expireDays,
                couponEntity.couponIssuanceCoverage,
                couponEntity.couponUseCoverage,
                couponEntity.couponUsableStandardAmount,
                couponEntity.usableStandardMount,
                couponEntity.couponUsableStandardAmount,
                couponEntity.issueStandardMount,
                couponEntity.paymentMethod,
                couponEntity.couponIssuancePeriod,
                couponEntity.startIssuanceAt,
                couponEntity.endIssuanceAt,
                couponEntity.numberOfWithPeriod,
                couponEntity.couponIssuance,
                couponEntity.loginCouponIssueNotification,
                couponEntity.issuingCouponsSendSMS,
                couponEntity.issuanceCouponSendEmail,
                couponEntity.status,
                couponEntity.modifyAt
        ))
                .from(couponEntity)
                .where(builder).orderBy(couponEntity.name.asc());

        if (couponSearchDto.getSearchCount() > 0) { // 쿠폰 등록 페이지에서는 LIMIT를 주지 않음
            query = query
                    .offset(couponSearchDto.getPageIndex() * couponSearchDto.getSearchCount())
                    .limit(couponSearchDto.getSearchCount());
        }

        List<CouponListDto> result = query.fetch();

        int count = result.size();

        return new PageImpl<>(result, pageable, count);
    }
}
