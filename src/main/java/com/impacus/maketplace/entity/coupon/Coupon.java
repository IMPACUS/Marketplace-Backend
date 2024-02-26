package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.coupon.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String code;    // 쿠폰 코드 (임시로 UUID)

    private String name;    // 쿠폰 이름

    private String desc;    // 쿠폰 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "cbc_code")
    private CouponBenefitClassification couponBenefitClassification;    // 혜택 구분 [ 원, % ]

    private BigDecimal benefitAmount;   // 혜택 금액 및 퍼센트

    @Enumerated(EnumType.STRING)
    @Column(name = "cic_code")
    private CouponIssuanceClassification couponIssuanceClassification;  // 발급 구분 [ 그린 태그, 유저 일반, 신규 고객 첫 주문, SNS 홍보 태그 ]

    @Enumerated(EnumType.STRING)
    @Column(name = "cpt_code")
    private CouponPaymentTarget couponPaymentTarget;    // 지급 대상 [ 모든 회원, 선찫순 ]

    private Long firstComeFirstServedAmount; // 선착순 발급 수

    @Enumerated(EnumType.STRING)
    @Column(name = "cit_code")
    private CouponIssuedTime couponIssuedTime;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]

    @Enumerated(EnumType.STRING)
    @Column(name = "cet_code")
    private CouponExpireTime couponExpireTime;  // 사용기간 [ 발급잉로 부터 N일, 무제한 ]

    private Long expireDays;    // 유효기간(일)

    @Enumerated(EnumType.STRING)
    @Column(name = "cc_issue_code")
    private CouponCoverage couponIssuanceCoverage;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "cc_use_code")
    private CouponCoverage couponUseCoverage;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "cusa_use_code")
    private CouponStandardAmountType couponUsableStandardAmount;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    private BigDecimal usableStandardMount; // N원 (N원 이상 주문시 사용 가능)

    @Enumerated(EnumType.STRING)
    @Column(name = "cusa_issue_code")
    private CouponStandardAmountType couponIssuanceStandardAmount;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    private BigDecimal issueStandardMount;  // N원 (N원 이상 주문시 쿠폰 발급)

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_code")
    private PaymentMethod paymentMethod;    // 결제수단 [ 카드, XXX 페이, 간편결제 ]

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_code")
    private CouponIssuancePeriodType couponIssuancePeriod;  // 기간 설정

    private LocalDate startIssuanceAt;  // 기간설정 시작 기간

    private LocalDate endIssuanceAt;    // 기간설정 종료 기간

    private Long numberOfWithPeriod;    // 기간 내 N 회 주문 시

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_code")
    private CouponIssuanceType couponIssuance;  // 자동 / 수동 발급 [ 자동, 수동 ]

    private String loginCouponIssueNotification = "N";  // 로그인 쿠폰 발급 알림

    private String issuingCouponsSendSMS = "N"; // 쿠폰발급 SMS 발송

    private String issuanceCouponSendEmail = "N";   // 쿠폰 발급 Email 발송


    private Boolean isActive = false;    // 활성화 여부

    @Version    // 낙관적 락 ( 여러 트랜잭션에서 유저에게 할당할 때 대비 => 최초 커밋만 인정)
    private Integer version;
}
