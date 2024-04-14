package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponBenefitClassification;
import com.impacus.maketplace.common.enumType.coupon.CouponExpireTime;
import com.impacus.maketplace.common.enumType.coupon.CouponStandardAmountType;
import com.impacus.maketplace.common.enumType.coupon.IssuanceStatus;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.request.CouponSearchDto;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.request.CouponUserSearchDto;
import com.impacus.maketplace.dto.coupon.response.*;
import com.impacus.maketplace.entity.coupon.*;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.entity.user.User;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.impacus.maketplace.common.utils.CouponUtils.fromCode;

@Repository
@RequiredArgsConstructor
public class CouponCustomRepositoryImpl implements CouponCustomRepository{

    private final JPAQueryFactory queryFactory;

    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QUser userEntity = QUser.user;
    private final QCoupon couponEntity = QCoupon.coupon;
    private final QCouponIssuanceClassificationData couponClassificationEntity = QCouponIssuanceClassificationData.couponIssuanceClassificationData;

    private final QCouponUser couponUserEntity = QCouponUser.couponUser;

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
            builder.and(couponEntity.status.eq(fromCode(IssuanceStatus.class, couponSearchDto.getOrderStatus().toLowerCase())));
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
                couponEntity.couponPaymentTarget,
                couponEntity.firstComeFirstServedAmount,
                couponEntity.couponIssuedTime,
                couponEntity.couponExpireTime,
                couponEntity.expireDays,
                couponEntity.couponIssuanceCoverage,
                couponEntity.couponUseCoverage,
                couponEntity.couponUsableStandardAmountType,
                couponEntity.usableStandardAmount,
                couponEntity.couponUsableStandardAmountType,
                couponEntity.issueStandardAmount,
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
                .leftJoin(couponClassificationEntity)
                .on(couponEntity.couponIssuanceClassificationData.eq(couponClassificationEntity))
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

    @Override
    public Page<CouponUserListDto> findAllCouponUserData(CouponUserSearchDto couponUserSearchDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        User user = User.builder().id(couponUserSearchDto.getUserId()).build();
        if (couponUserSearchDto.getSearchType().equals("ALL")) {
            builder.and(couponUserEntity.isUsed.eq(false)).and(couponUserEntity.expiredAt.after(LocalDateTime.now()))
                    .and(couponUserEntity.couponLock.eq(false))
                    .and(couponUserEntity.user.eq(user))
                    .or(couponUserEntity.availableDownloadAt.isNotNull());
        } else {

        }
        String searchValue = couponUserSearchDto.getSearchValue();
        couponUserSearchDto.setSearchValue(searchValue.trim());
        if (!searchValue.isEmpty() && !searchValue.isBlank()) {
            builder.and(couponEntity.name.like("%" + couponUserSearchDto.getSearchValue() + "%"));
        }

        JPAQuery<CouponUser> query = queryFactory.selectFrom(couponUserEntity)
                .innerJoin(couponEntity)
                .on(couponEntity.id.eq(couponUserEntity.coupon.id))
                .where(builder);

        if (couponUserSearchDto.getSortType().equals("RECENT")) {
            query = query.orderBy(couponUserEntity.createAt.desc());
        } else if (couponUserSearchDto.getSortType().equals("PRICE")) {
            query = query.orderBy(couponEntity.couponBenefitClassification.desc(), couponEntity.benefitAmount.desc());
        }

        List<CouponUser> data = query.fetch();

        List<CouponUserListDto> result = data.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
        Long count = getCouponUserCount(couponUserSearchDto);

        return new PageImpl<>(result, pageable, count);
    }

    public static CouponUserListDto entityToDto(CouponUser couponUser) {
        String price;   // ex) 20,000원
        String name;    // ex) 환경을 위한 감사 쿠폰
        String desc;    // ex) 13만원 이상의 제품 구매시 사용 가능
        String expireDate;  //ex) 24.05.24 23:59까지
        String buttonValue;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

        Coupon coupon = couponUser.getCoupon();
        if (coupon.getCouponBenefitClassification() == CouponBenefitClassification.AMOUNT) {
            price = String.format("%,d", coupon.getBenefitAmount()) + "원 할인";
        } else if (coupon.getCouponBenefitClassification() == CouponBenefitClassification.PERCENTAGE) {
            price = coupon.getBenefitAmount() + "% 할인";
        } else {
            throw new CustomException(ErrorType.INVALID_COUPON_FORMAT);
        }

        name = coupon.getName();
        if (coupon.getCouponUsableStandardAmountType() == CouponStandardAmountType.LIMIT) {
            desc = CouponStandardAmountType.LIMIT.getValue().replaceAll("N", String.valueOf(coupon.getUsableStandardAmount()));
        } else if (coupon.getCouponUsableStandardAmountType() == CouponStandardAmountType.UNLIMITED) {
            desc = CouponStandardAmountType.UNLIMITED.getValue();
        } else {
            throw new CustomException(ErrorType.INVALID_COUPON_FORMAT);
        }
        if (coupon.getCouponExpireTime() == CouponExpireTime.LIMIT) {
            expireDate = dtf.format(couponUser.getExpiredAt())+"까지";
        } else if (coupon.getCouponExpireTime() == CouponExpireTime.UNLIMITED){
            expireDate = CouponExpireTime.UNLIMITED.getValue();
        } else {
            throw new CustomException(ErrorType.INVALID_COUPON_FORMAT);
        }
        /**
         * 쿠폰에 락이 걸려 있고, availDownloadedAt이 null 이 아니라면, N일 뒤 쿠폰
         */
        String availableDownloadDate = null;
        if (couponUser.getCouponLock() == true && couponUser.getAvailableDownloadAt() != null) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            availableDownloadDate = dtf.format(couponUser.getAvailableDownloadAt());
            buttonValue = "일 뒤 쿠 발급 가능";
        } else if (couponUser.getCouponLock() == true && couponUser.getAvailableDownloadAt() == null) {
            buttonValue = "ERROR";
        } else if (couponUser.getIsUsed() == false){
            buttonValue = "쿠폰 다운 받기";
        } else {
            buttonValue = "쿠폰 다운 받기 완료";
        }

        return CouponUserListDto.builder()
                .id(couponUser.getId())
                .price(price)
                .name(name)
                .desc(desc)
                .expireDate(expireDate)
                .isDownloaded(couponUser.getIsDownloaded())
                .availableDownloadAt(availableDownloadDate)
                .couponLock(couponUser.getCouponLock())
                .buttonValue(buttonValue)
                .couponBenefitClassification(coupon.getCouponBenefitClassification())
                .benefitAmount(coupon.getBenefitAmount())
                .couponPaymentTarget(coupon.getCouponPaymentTarget())
                .firstComeFirstServedAmount(coupon.getFirstComeFirstServedAmount())
                .couponExpireTime(coupon.getCouponExpireTime())
                .couponUsableStandardAmountType(coupon.getCouponUsableStandardAmountType())
                .usableStandardAmount(coupon.getUsableStandardAmount())
                .createAt(dtf.format(couponUser.getCreateAt()))
                .couponType(coupon.getCouponType())
                .build();
    }

    private Long getCouponUserCount(CouponUserSearchDto couponUserSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();
        User user = User.builder().id(couponUserSearchDto.getUserId()).build();

        if (couponUserSearchDto.getSearchType().equals("ALL")) {
            builder.and(couponUserEntity.isUsed.eq(false)).and(couponUserEntity.expiredAt.after(LocalDateTime.now()))
                    .and(couponUserEntity.couponLock.eq(false))
                    .and(couponUserEntity.user.eq(user))
                    .or(couponUserEntity.availableDownloadAt.isNotNull());
        } else {

        }
        String searchValue = couponUserSearchDto.getSearchValue();
        couponUserSearchDto.setSearchValue(searchValue.trim());
        if (!searchValue.isEmpty() && !searchValue.isBlank()) {
            builder.and(couponEntity.name.like("%" + couponUserSearchDto.getSearchValue() + "%"));
        }
        return queryFactory.select(couponUserEntity.count())
                .from(couponUserEntity)
                .innerJoin(couponEntity)
                .on(couponEntity.id.eq(couponUserEntity.coupon.id))
                .where(builder)
                .fetchOne();

    }
}
