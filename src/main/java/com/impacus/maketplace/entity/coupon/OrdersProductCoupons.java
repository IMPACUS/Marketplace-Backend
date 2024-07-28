package com.impacus.maketplace.entity.coupon;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrdersProductCoupons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_product_coupons_id")
    private Long id;

    @Column(name = "orders_product_id", nullable = false)
    private Long ordersProductId;

    @Column(name = "user_coupon_id", nullable = false)
    private Long userCouponId;
}
