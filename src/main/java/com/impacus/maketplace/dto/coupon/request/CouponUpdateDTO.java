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
public class CouponUpdateDTO {

    @NotNull
    private Long id;
    @NotNull
    private String benefitType;    // 혜택 구분 [ 원, % ]
    @NotNull
    private int benefitValue;   // 혜택 금액 및 퍼센트

    @NotNull
    private String productTargetType;   //  ECO 상품 여부 [ECO할인,그린태그 , 일반 상품, 구분안함]
    @NotNull
    private String paymentTargetType;    // 지급 대상 [ 모든 회원, 선착순 ]

    @Builder.Default
    private Long firstCount = 0L; // 선착순 발급 수

    @NotNull
    private String issuedTimeType;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
    @NotNull
    private String type;              // 쿠폰 형식 [ 이벤트 , 지급형 ]
    @NotNull
    private String issueCoverageType;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
    @NotNull
    private String useCoverageType;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;


    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;

    @NotNull
    private String useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    @Builder.Default
    private int useStandardValue = 0; // N원 (N원 이상 주문시 사용 가능)

    @NotNull
    private String issueStandardType;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    private int issueStandardValue = 0;  // N원 (N원 이상 주문시 쿠폰 발급)
    @NotNull
    private String periodType;  // 기간 설정

    private String periodStartAt;  // 기간설정 시작 기간

    private String periodEndAt;    // 기간설정 종료 기간

    @Builder.Default
    private Long numberOfPeriod = 0L;    // 기간 내 N 회 주문 시

    @NotNull
    private String autoManualType;  // 자동 / 수동 발급 [ 자동, 수동 ]

    //TODO: 수정할때 필요하지 않을까?
    private String expireTimeType;  // 사용기간 [ 발급일로 부터 N일, 무제한 ]
    private Long expireDays = -1L;    // 유효기간(일)

    private String code;    //  변경할 쿠폰 코드

    @Builder.Default
    @NotNull
    private String loginAlert = "N";  // 로그인 쿠폰 발급 알림

    @Builder.Default
    @NotNull
    private String smsAlert = "N"; // 쿠폰발급 SMS 발송

    @Builder.Default
    @NotNull
    private String emailAlert = "N";   // 쿠폰 발급 Email 발송

}

