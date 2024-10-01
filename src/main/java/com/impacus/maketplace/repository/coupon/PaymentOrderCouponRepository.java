package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.PaymentOrderCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderCouponRepository extends JpaRepository<PaymentOrderCoupon, Long> {
}
