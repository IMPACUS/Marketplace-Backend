package com.impacus.maketplace.entity.coupon;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Table(name = "payment_order_coupons")
public class PaymentOrderCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_order_coupons_id")
    private Long id;

    @Column(nullable = false)
    private Long paymentOrderId;

    @Column(nullable = false)
    private Long userCouponId;
}
