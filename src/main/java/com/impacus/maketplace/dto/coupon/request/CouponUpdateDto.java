package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponUpdateDto {

    @NotNull
    private Long id;
    @NotNull
    private String couponBenefitClassificationType;    // 혜택 구분 [ 원, % ]
    @NotNull
    private int benefitAmount;   // 혜택 금액 및 퍼센트
    @NotNull
    private String couponIssuanceClassificationType;  // 발급 구분 [ 그린 태그, 유저 일반, 신규 고객 첫 주문, SNS 홍보 태그 ]

    private Long couponIssuanceClassificationData; // 발급 구분 별 데이터
    @NotNull
    private String couponPaymentTargetType;    // 지급 대상 [ 모든 회원, 선착순 ]

    private Long firstComeFirstServedAmount = 0L; // 선착순 발급 수
    @NotNull
    private String couponIssuedTimeType;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
    @NotNull
    private String couponType;              // 쿠폰 형식 [ 이벤트 , 지급형 ]
    @NotNull
    private String couponIssuanceCoverageType;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
    @NotNull
    private String couponUseCoverageType;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;


    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;

    @NotNull
    private String couponUsableStandardAmountType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    private int usableStandardAmount = 0; // N원 (N원 이상 주문시 사용 가능)
    @NotNull
    private String couponIssuanceStandardAmountType;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    private int issueStandardAmount = 0;  // N원 (N원 이상 주문시 쿠폰 발급)
    @NotNull
    private String couponIssuancePeriodType;  // 기간 설정

    private String startIssuanceAt;  // 기간설정 시작 기간

    private String endIssuanceAt;    // 기간설정 종료 기간

    private Long numberOfWithPeriod = 0L;    // 기간 내 N 회 주문 시

    @NotNull
    private String couponIssuanceType;  // 자동 / 수동 발급 [ 자동, 수동 ]

    @Builder.Default
    @NotNull
    private String loginCouponIssueNotification = "N";  // 로그인 쿠폰 발급 알림

    @Builder.Default
    @NotNull
    private String issuingCouponsSendSMS = "N"; // 쿠폰발급 SMS 발송

    @Builder.Default
    @NotNull
    private String issuanceCouponSendEmail = "N";   // 쿠폰 발급 Email 발송

}

