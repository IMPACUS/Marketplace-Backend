package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponTriggerType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CouponTrigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_trigger_id")
    private Long id;

    @Column(nullable = false)
    private Long userCouponId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponTriggerType triggerType;

    @Column(nullable = false)
    private Long triggerId;
}
