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
    private CouponBenefitClassification couponBenefitClassification;    // 혜택 구분 [ 원, % ]
    private int benefitAmount;   // 혜택 금액 및 퍼센트
    private CouponIssuanceClassification couponIssuanceClassification;  // 발급 구분 [ 그린 태그, 유저 일반, 신규 고객 첫 주문, SNS 홍보 태그 ]
    private CouponPaymentTarget couponPaymentTarget;    // 지급 대상 [ 모든 회원, 선착순 ]
    private Long firstComeFirstServedAmount = 0L; // 선착순 발급 수
    private CouponIssuedTime couponIssuedTime;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
    private CouponExpireTime couponExpireTime;  // 사용기간 [ 발급잉로 부터 N일, 무제한 ]
    private Long expireDays = 0L;    // 유효기간(일)
    private CouponCoverage couponIssuanceCoverage;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;
    private CouponCoverage couponUseCoverage;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;
    private CouponStandardAmountType couponUsableStandardAmountType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private int usableStandardAmount = 0; // N원 (N원 이상 주문시 사용 가능)
    private CouponStandardAmountType couponIssuanceStandardAmountType;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private int issueStandardAmount = 0;  // N원 (N원 이상 주문시 쿠폰 발급)
    private CouponIssuancePeriodType couponIssuancePeriod;  // 기간 설정
    private LocalDate startIssuanceAt;  // 기간설정 시작 기간
    private LocalDate endIssuanceAt;    // 기간설정 종료 기간
    private Long numberOfWithPeriod = 0L;    // 기간 내 N 회 주문 시
    private CouponIssuanceType couponIssuance;  // 자동 / 수동 발급 [ 자동, 수동 ]
    private String loginCouponIssueNotification = "N";  // 로그인 쿠폰 발급 알림
    private String issuingCouponsSendSMS = "N"; // 쿠폰발급 SMS 발송
    private String issuanceCouponSendEmail = "N";   // 쿠폰 발급 Email 발송
    private IssuanceStatus status;
    private LocalDateTime modifyAt;

    // code To String
    private String issuanceStandard;    // 지급 조건
    private String expiredPeriod;      // 사용기간
    private String numberOfIssuance;    // 발급수
    private String manualOrAutomatic;   // 자동/수동
    private String issuanceStatus;      // 발급 상태
    private String recentActivity;      // 최근활동


    @QueryProjection
    public CouponListDto(Long id, String code, String name, String description, CouponBenefitClassification couponBenefitClassification, int benefitAmount, CouponIssuanceClassification couponIssuanceClassification, CouponPaymentTarget couponPaymentTarget, Long firstComeFirstServedAmount, CouponIssuedTime couponIssuedTime, CouponExpireTime couponExpireTime, Long expireDays, CouponCoverage couponIssuanceCoverage, CouponCoverage couponUseCoverage, CouponStandardAmountType couponUsableStandardAmount, int usableStandardMount, CouponStandardAmountType couponIssuanceStandardAmountType, int issueStandardAmount, CouponIssuancePeriodType couponIssuancePeriod, LocalDate startIssuanceAt, LocalDate endIssuanceAt, Long numberOfWithPeriod, CouponIssuanceType couponIssuance, String loginCouponIssueNotification, String issuingCouponsSendSMS, String issuanceCouponSendEmail, IssuanceStatus status, LocalDateTime modifyAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.couponBenefitClassification = couponBenefitClassification;
        this.benefitAmount = benefitAmount;
        this.couponIssuanceClassification = couponIssuanceClassification;
        this.couponPaymentTarget = couponPaymentTarget;
        this.firstComeFirstServedAmount = firstComeFirstServedAmount;
        this.couponIssuedTime = couponIssuedTime;
        this.couponExpireTime = couponExpireTime;
        this.expireDays = expireDays;
        this.couponIssuanceCoverage = couponIssuanceCoverage;
        this.couponUseCoverage = couponUseCoverage;
        this.couponUsableStandardAmountType = couponUsableStandardAmount;
        this.usableStandardAmount = usableStandardMount;
        this.couponIssuanceStandardAmountType = couponIssuanceStandardAmountType;
        this.issueStandardAmount = issueStandardAmount;
        this.couponIssuancePeriod = couponIssuancePeriod;
        this.startIssuanceAt = startIssuanceAt;
        this.endIssuanceAt = endIssuanceAt;
        this.numberOfWithPeriod = numberOfWithPeriod;
        this.couponIssuance = couponIssuance;
        this.loginCouponIssueNotification = loginCouponIssueNotification;
        this.issuingCouponsSendSMS = issuingCouponsSendSMS;
        this.issuanceCouponSendEmail = issuanceCouponSendEmail;
        this.status = status;
        this.modifyAt = modifyAt;
    }
}
