package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.PaymentEventCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEventCouponRepository extends JpaRepository<PaymentEventCoupon, Long> {
}
