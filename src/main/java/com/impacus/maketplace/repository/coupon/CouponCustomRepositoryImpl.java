package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponBenefitType;
import com.impacus.maketplace.common.enumType.coupon.CouponExpireTimeType;
import com.impacus.maketplace.common.enumType.coupon.CouponStandardType;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.response.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.impacus.maketplace.entity.coupon.QCouponUser;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.entity.user.QUserStatusInfo;
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
public class CouponCustomRepositoryImpl implements CouponCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QUser userEntity = QUser.user;
    private final QCoupon couponEntity = QCoupon.coupon;
    private final QCouponUser couponUserEntity = QCouponUser.couponUser;
    private final QUserStatusInfo userStatusInfo = QUserStatusInfo.userStatusInfo;


    @Override
    public CouponUserInfoResponseDTO findByAddCouponInfo(String provideTarget, String userEmail) {
        CouponUserInfoResponseDTO result = queryFactory.select(new QCouponUserInfoResponseDTO(
                        userEntity.id,
                        userEntity.name,
                        Expressions.enumPath(UserStatus.class, userStatusInfo.status.toString()),
                        pointMasterEntity.availablePoint,
                        pointMasterEntity.userScore,
                        Expressions.constant("010-0000-0000"),  //TODO: 휴대폰 번호 추가해야함
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM-DD HH24:MI:SS')", userEntity.createAt),
                        Expressions.constant("profile-path"),   //TODO: 프로필 경로 추가 예정
                        userEntity.email
                )).from(pointMasterEntity)
                .innerJoin(userEntity).on(userEntity.id.eq(pointMasterEntity.user.id))
                .leftJoin(userStatusInfo).on(userStatusInfo.userId.eq(userEntity.id))
                .where(userEntity.email.eq(userEmail))
                .fetchOne();

        return result;
    }

    @Override
    public Page<CouponListDTO> findAllCouponList(String searchValue, String searchOrder, Pageable pageable) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (StringUtils.isNotBlank(searchValue)) {
            conditionBuilder.and(couponEntity.name.like("%" + searchValue + "%"));
        }
        if (StringUtils.isNotBlank(searchOrder)) {
            conditionBuilder.and(couponEntity.statusType.eq(fromCode(CouponStatusType.class, searchOrder)));
        }

        JPAQuery<CouponListDTO> query = queryFactory.select(new QCouponListDTO(
                        couponEntity.id,
                        couponEntity.code,
                        couponEntity.name,
                        couponEntity.description,
                        couponEntity.benefitType,
                        couponEntity.benefitValue,
                        couponEntity.productTargetType,
                        couponEntity.paymentTargetType,
                        couponEntity.firstCount,
                        couponEntity.issuedTimeType,
                        couponEntity.expireTimeType,
                        couponEntity.expireDays,
                        couponEntity.issueCoverageType,
                        couponEntity.useCoverageType,
                        couponEntity.useStandardType,
                        couponEntity.useStandardValue,
                        couponEntity.issueStandardType,
                        couponEntity.issueStandardValue,
                        couponEntity.periodType,
                        couponEntity.periodStartAt,
                        couponEntity.periodEndAt,
                        couponEntity.numberOfPeriod,
                        couponEntity.autoManualType,
                        couponEntity.loginAlert,
                        couponEntity.smsAlert,
                        couponEntity.emailAlert,
                        couponEntity.statusType,
                        couponEntity.modifyAt
                ))
                .from(couponEntity)
                .where(conditionBuilder).orderBy(couponEntity.name.asc());

        if (pageable.getPageSize() > 0) { // 쿠폰 등록 페이지에서는 LIMIT를 주지 않음
            query = query
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        List<CouponListDTO> result = query.fetch();

        Long count = getCouponListDTOCount(searchValue, searchOrder);

        return new PageImpl<>(result, pageable, count);
    }

    private Long getCouponListDTOCount(String searchValue, String searchOrder) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (StringUtils.isNotBlank(searchValue)) {
            conditionBuilder.and(couponEntity.name.like("%" + searchValue + "%"));
        }
        if (StringUtils.isNotBlank(searchOrder)) {
            conditionBuilder.and(couponEntity.statusType.eq(fromCode(CouponStatusType.class, searchOrder)));
        }
        return queryFactory.select(couponEntity.count())
                .where(conditionBuilder)
                .fetchOne();
    }

    @Override
    public Page<CouponUserListDTO> findAllCouponUserData(String searchValue, String searchOrder, Long userId, Pageable pageable) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        User user = User.builder().id(userId).build();

        conditionBuilder.and(couponUserEntity.isUsed.eq(false)).and(couponUserEntity.expiredAt.after(LocalDateTime.now()))
                .and(couponUserEntity.couponLock.eq(false))
                .and(couponUserEntity.user.eq(user))
                .or(couponUserEntity.availableDownloadAt.isNotNull());

        if (StringUtils.isNotBlank(searchValue)) {
            conditionBuilder.and(couponEntity.name.like("%" + searchValue + "%"));
        }

        JPAQuery<CouponUser> query = queryFactory.selectFrom(couponUserEntity)
                .innerJoin(couponEntity)
                .on(couponEntity.id.eq(couponUserEntity.coupon.id))
                .where(conditionBuilder);

        if (StringUtils.isNotBlank(searchOrder)) {
            if (searchOrder.equals("RECENT")) {
                query = query.orderBy(couponUserEntity.createAt.desc());
            } else if (searchOrder.equals("PRICE")) {
                query = query.orderBy(couponEntity.benefitType.desc(), couponEntity.benefitValue.desc(), couponUserEntity.createAt.desc());
            }
        } else {
            query = query.orderBy(couponUserEntity.createAt.desc());
        }

        List<CouponUser> data = query.limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        List<CouponUserListDTO> result = data.stream().map(entity -> toDto(entity)).collect(Collectors.toList());
        Long count = getCouponUserDataCount(searchValue, userId);

        return new PageImpl<>(result, pageable, count);
    }
    private Long getCouponUserDataCount(String searchValue, Long userId) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        User user = User.builder().id(userId).build();

        conditionBuilder.and(couponUserEntity.isUsed.eq(false)).and(couponUserEntity.expiredAt.after(LocalDateTime.now()))
                .and(couponUserEntity.couponLock.eq(false))
                .and(couponUserEntity.user.eq(user))
                .or(couponUserEntity.availableDownloadAt.isNotNull());

        if (StringUtils.isNotBlank(searchValue)) {
            conditionBuilder.and(couponEntity.name.like("%" + searchValue + "%"));
        }

        return queryFactory.select(couponUserEntity.count())
                .from(couponUserEntity)
                .innerJoin(couponEntity)
                .on(couponEntity.id.eq(couponUserEntity.coupon.id))
                .where(conditionBuilder)
                .fetchOne();

    }

    public static CouponUserListDTO toDto(CouponUser couponUser) {
        String price;   // ex) 20,000원
        String name;    // ex) 환경을 위한 감사 쿠폰
        String desc;    // ex) 13만원 이상의 제품 구매시 사용 가능
        String expireDate;  //ex) 24.05.24 23:59까지
        String buttonValue;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

        Coupon coupon = couponUser.getCoupon();
        if (coupon.getBenefitType() == CouponBenefitType.AMOUNT) {
            price = String.format("%,d", coupon.getBenefitValue()) + "원";
        } else if (coupon.getBenefitType() == CouponBenefitType.PERCENTAGE) {
            price = coupon.getBenefitValue() + "%";
        } else {
            throw new CustomException(CouponErrorType.INVALID_COUPON_FORMAT);
        }

        name = coupon.getName();
        if (coupon.getUseStandardType() == CouponStandardType.LIMIT) {
            desc = CouponStandardType.LIMIT.getValue().replaceAll("N", String.valueOf(coupon.getUseStandardValue()));
        } else if (coupon.getUseStandardType() == CouponStandardType.UNLIMITED) {
            desc = CouponStandardType.UNLIMITED.getValue();
        } else {
            throw new CustomException(CouponErrorType.INVALID_COUPON_FORMAT);
        }
        if (coupon.getExpireTimeType() == CouponExpireTimeType.LIMIT) {
            expireDate = dtf.format(couponUser.getExpiredAt())+"까지";
        } else if (coupon.getExpireTimeType() == CouponExpireTimeType.UNLIMITED){
            expireDate = CouponExpireTimeType.UNLIMITED.getValue();
        } else {
            throw new CustomException(CouponErrorType.INVALID_COUPON_FORMAT);
        }
        /**
         * 쿠폰에 락이 걸려 있고, availDownloadedAt이 null 이 아니라면, N일 뒤 쿠폰
         */
        String availableDownloadDate = null;
        if (couponUser.getCouponLock() == true && couponUser.getAvailableDownloadAt() != null) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            availableDownloadDate = dtf.format(couponUser.getAvailableDownloadAt());
            buttonValue = "일 뒤 발급 가능";
        } else if (couponUser.getCouponLock() == true && couponUser.getAvailableDownloadAt() == null) {
            buttonValue = "ERROR";
        } else if (couponUser.getIsUsed() == false){
            buttonValue = "쿠폰 다운 받기";
        } else {
            buttonValue = "쿠폰 다운 받기 완료";
        }

        return CouponUserListDTO.builder()
                .id(couponUser.getId())
                .price(price)
                .name(name)
                .desc(desc)
                .expireDate(expireDate)
                .isDownloaded(couponUser.getIsDownloaded())
                .availableDownloadAt(availableDownloadDate)
                .couponLock(couponUser.getCouponLock())
                .buttonValue(buttonValue)
                .benefitType(coupon.getBenefitType())
                .benefitValue(coupon.getBenefitValue())
                .paymentTargetType(coupon.getPaymentTargetType())
                .firstCount(coupon.getFirstCount() != null ? coupon.getFirstCount() : null)
                .expireTimeType(coupon.getExpireTimeType())
                .useStandardType(coupon.getUseStandardType())
                .useStandardValue(coupon.getUseStandardValue() != null ? coupon.getUseStandardValue() : null)
                .createAt(dtf.format(couponUser.getCreateAt()))
                .type(coupon.getType())
                .build();
    }


}
