package com.impacus.maketplace.entity.coupon;

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
    private Long issuedCouponHistoryId;

    @Column(nullable = false)
    private Long triggerId;
}
