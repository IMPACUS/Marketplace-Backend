package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.coupon.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "couponUsers")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponUser> couponUsers;

    private String code;    // 쿠폰 코드 (임시로 UUID)

    private String name;    // 쿠폰 이름

    @Column(name = "coupon_desc")
    private String description;    // 쿠폰 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type")
    private CouponBenefitType benefitType;    // 혜택 구분 [ 원, % ]

    @Column(name = "benefit_value")
    private int benefitValue;   // 혜택 금액 및 퍼센트

    @Column(name = "product_target")
    private CouponProductTargetType productTargetType;    //  ECO 상품 여부 [ECO할인,그린태그 , 일반 상품, 구분안함]

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private CouponPaymentTargetType paymentTargetType;    // 지급 대상 [ 모든 회원, 선착순 ]

    @Column(name = "first_count")
    @Builder.Default
    private Long firstCount = null; // 선착순 발급 수

    @Enumerated(EnumType.STRING)
    @Column(name = "issued_type")
    private CouponIssuedTimeType issuedTimeType;  // 발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType type;              // 쿠폰 형식 [ 이벤트 , 지급형 ]

    @Enumerated(EnumType.STRING)
    @Column(name = "expire_type")
    private CouponExpireTimeType expireTimeType;  // 사용기간 [ 발급일로 부터 N일, 무제한 ]


    @Builder.Default
    private Long expireDays = -1L;    // 유효기간(일)

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_coverage_type")
    private CouponCoverageType issueCoverageType;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand issueBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_coverage_type")
    private CouponCoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]

    //TODO: 브랜드 추가시 브랜드 코드추가 예정
//    private Brand useBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_standard_type")
    private CouponStandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    @Builder.Default
    @Column(name = "use_standard_value")
    private Integer useStandardValue = 0; // N원 (N원 이상 주문시 사용 가능)

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_standard_type")
    private CouponStandardType issueStandardType;  // 쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]

    @Builder.Default
    @Column(name = "issue_standard_value")
    private Integer issueStandardValue = 0;  // N원 (N원 이상 주문시 쿠폰 발급)

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private CouponPeriodType periodType;  // 기간 설정

    @Setter
    @Column(name = "period_start")
    private LocalDate periodStartAt;  // 기간설정 시작 기간

    @Setter
    @Column(name = "period_end")
    private LocalDate periodEndAt;    // 기간설정 종료 기간

    @Builder.Default
    @Setter
    @Column(name = "number_of_period")
    private Long numberOfPeriod = 0L;    // 기간 내 N 회 주문 시

    @Enumerated(EnumType.STRING)
    @Column(name = "auto_manual")
    private CouponAutoManualType autoManualType;  // 자동 / 수동 발급 [ 자동, 수동 ]

    @Builder.Default
    @Column(name = "login_alert")
    private String loginAlert = "N";  // 로그인 쿠폰 발급 알림

    @Builder.Default
    @Column(name = "sms_alert")
    private String smsAlert = "N"; // 쿠폰발급 SMS 발송

    @Builder.Default
    @Column(name = "email_alert")
    private String emailAlert = "N";   // 쿠폰 발급 Email 발송

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CouponStatusType statusType = CouponStatusType.ISSUING;    // 발급 상태

//    @Version    // 낙관적 락 ( 여러 트랜잭션에서 유저에게 할당할 때 대비 => 최초 커밋만 인정)
//    private Integer version;
}
