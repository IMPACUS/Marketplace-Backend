package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.coupon.CouponBenefitClassification;
import com.impacus.maketplace.service.CouponService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
//@Table(
//        name = "coupon",
//        indexes = {@Index(name = "coupon_expired_at_index", columnList = "expired_at", unique = false)}
//)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Pattern(regexp = CouponService.COUPON_CODE)
    private String code;

    private LocalDateTime expiredAt;    // 쿠폰 만료 날짜

    private Integer discount;

    @Enumerated(EnumType.STRING)
    private CouponBenefitClassification couponType;  // 쿠폰 타입 : [ 금액, 퍼센트 ]

    private Integer constraints; //  쿠폰 사용 제약 조건 ( 최소 이용 금액 )

    @Builder.Default
    private Boolean isActive = true; // 해당 쿠폰을 관리자가 정지할 수 있게 만든 칼럼

    @Version // 낙관적 락 ( 여러 트랜잭션에서 유저에게 할당할 때 대비 => 최최 커밋만 인정)
    private Integer version;




}
