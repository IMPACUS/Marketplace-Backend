package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.coupon.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;    // PK

    @Column(unique = true, nullable = false)
    private String code;    // 쿠폰 코드

    @Column(nullable = false)
    private String name;    // 쿠폰 이름

    private String description; // 쿠폰 설명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitType benefitType;  // 혜택 구분 [ 원, % ]

    @Column(nullable = false)
    private Integer benefitValue;   // 혜택 금액 및 퍼센트

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;    //  상품 적용 타입 [ ECO할인/그린태그 , 일반 상품, 구분안함(둘다 적용) ]

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentTarget paymentType;   // 지급 대상 [ 모든 회원, 선착순 ]

    private Integer firstCount;  // 선착순 발급 수

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long quantityIssued;   // 발급한 수량

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuedTimeType issuedTimeType;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType;  // 쿠폰 형식 [ 이벤트 , 지급형 ]

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpireTimeType expireTimeType;  // 사용기간 타입 [ 발급일로 부터 N일, 무제한 ]

    @ColumnDefault("0")
    private Integer expireTimeDays; // 사용기간(일)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoverageType issueCoverageType; // 발급 적용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]

    @Column(name = "issue_coverage_super_category_id")
    private Long issueCoverageSuperCategoryId;   // 발급 적용 범위 1차 카테고리 id

    @Column(name = "issue_coverage_sub_category_id")
    private Long issueCoverageSubCategoryId;    // 발급 적용 범위 2차 카테고리 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]

    @Column(name = "use_coverage_super_category_id")
    private Long useCoverageSuperCategoryId;    // 쿠폰 사용 범위 1차 카테고리 id

    @Column(name = "use_coverage_sub_category_id")
    private Long useCoverageSubCategoryId;  // 쿠폰 사용 범위 2차 카테고리 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    @ColumnDefault("0")
    private Long useStandardValue;  // N원 (N원 이상 주문시 사용 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandardType issueStandardType; // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    @ColumnDefault("0")
    private Long issueStandardValue;    // N원 (N원 이상 주문시 쿠폰 발급)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;  // 기간 설정 [ 지정 기간 설정, 지정 기간 없음(무제한) ]

    private LocalDate periodStartAt;    // 기간 설정 시작 일자

    private LocalDate periodEndAt;  // 기간 설정 종료 일자

    private Long numberOfPeriod;    // 기간 내 N 회 주문 시

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutoManualType autoManualType;  // 자동/수동 발급 [ 자동, 수동 ]

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean loginAlarm; // 로그인 시 쿠폰 발급 알림

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean smsAlarm; // 쿠폰 발급 SMS 발송

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean emailAlarm; // 쿠폰 발급 이메일 발송

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean kakaoAlarm; // 쿠폰 발급 카톡 발송

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ISSUING'")
    private CouponStatusType statusType; // 발급 상태 [ 발급 중, 발급 대기, 발급 중지 ]
}
