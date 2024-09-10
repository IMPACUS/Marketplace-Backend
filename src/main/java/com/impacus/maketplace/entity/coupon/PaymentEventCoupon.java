package com.impacus.maketplace.entity.coupon;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "payment_event_coupons")
public class PaymentEventCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_event_coupons_id")
    private Long id;

    @Column(nullable = false)
    private Long paymentEventId;

    @Column(nullable = false)
    private Long userCouponId;
}
