package com.impacus.maketplace.dto.coupon.response;


import com.impacus.maketplace.common.enumType.coupon.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CouponListDto {

    private Long id;
//    private List<CouponUser> couponUsers;
    private String code;    // 쿠폰 코드 (임시로 UUID)
    private String name;    // 쿠폰 이름
    private String description;    // 쿠폰 설명
    private CouponBenefitType benefitType;    // 혜택 구분 [ 원, % ]
    private int benefitValue;   // 혜택 금액 및 퍼센트
    private CouponProductTargetType productTargetType;
    private CouponPaymentTargetType paymentTargetType;    // 지급 대상 [ 모든 회원, 선착순 ]
    private Long firstCount = 0L; // 선착순 발급 수
    private CouponIssuedTimeType issuedTimeType;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
    private CouponExpireTimeType expireTimeType;  // 사용기간 [ 발급잉로 부터 N일, 무제한 ]
    private Long expireDays = 0L;    // 유효기간(일)
    private CouponCoverageType issueCoverageType;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;
    private CouponCoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;
    private CouponStandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private Integer useStandardValue = 0; // N원 (N원 이상 주문시 사용 가능)
    private CouponStandardType issueStandardType;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private int issueStandardValue = 0;  // N원 (N원 이상 주문시 쿠폰 발급)
    private CouponPeriodType periodType;  // 기간 설정
    private LocalDate periodStartAt;  // 기간설정 시작 기간
    private LocalDate periodEndAt;    // 기간설정 종료 기간
    private Long numberOfPeriod = 0L;    // 기간 내 N 회 주문 시
    private CouponAutoManualType autoManualType;  // 자동 / 수동 발급 [ 자동, 수동 ]
    private String loginAlert = "N";  // 로그인 쿠폰 발급 알림
    private String smsAlert = "N"; // 쿠폰발급 SMS 발송
    private String emailAlert = "N";   // 쿠폰 발급 Email 발송
    private CouponStatusType statusType;
    private LocalDateTime modifyAt;

    // code To String
    private String issuanceStandard;    // 지급 조건
    private String expiredPeriod;      // 사용기간
    private String numberOfIssuance;    // 발급수
    private String manualOrAutomatic;   // 자동/수동
    private String issuanceStatus;      // 발급 상태
    private String recentActivity;      // 최근활동


    @QueryProjection

    public CouponListDto(Long id, String code, String name, String description, CouponBenefitType benefitType, int benefitValue, CouponProductTargetType productTargetType, CouponPaymentTargetType paymentTargetType, Long firstCount, CouponIssuedTimeType issuedTimeType, CouponExpireTimeType expireTimeType, Long expireDays, CouponCoverageType issueCoverageType, CouponCoverageType useCoverageType, CouponStandardType useStandardType, Integer useStandardValue, CouponStandardType issueStandardType, int issueStandardValue, CouponPeriodType periodType, LocalDate periodStartAt, LocalDate periodEndAt, Long numberOfPeriod, CouponAutoManualType autoManualType, String loginAlert, String smsAlert, String emailAlert, CouponStatusType statusType, LocalDateTime modifyAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.productTargetType = productTargetType;
        this.paymentTargetType = paymentTargetType;
        this.firstCount = firstCount;
        this.issuedTimeType = issuedTimeType;
        this.expireTimeType = expireTimeType;
        this.expireDays = expireDays;
        this.issueCoverageType = issueCoverageType;
        this.useCoverageType = useCoverageType;
        this.useStandardType = useStandardType;
        this.useStandardValue = useStandardValue;
        this.issueStandardType = issueStandardType;
        this.issueStandardValue = issueStandardValue;
        this.periodType = periodType;
        this.periodStartAt = periodStartAt;
        this.periodEndAt = periodEndAt;
        this.numberOfPeriod = numberOfPeriod;
        this.autoManualType = autoManualType;
        this.loginAlert = loginAlert;
        this.smsAlert = smsAlert;
        this.emailAlert = emailAlert;
        this.statusType = statusType;
        this.modifyAt = modifyAt;
    }
}
