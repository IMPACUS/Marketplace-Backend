package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.PaymentEventCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentEventCouponRepository extends JpaRepository<PaymentEventCoupon, Long> {

    List<PaymentEventCoupon> findByPaymentEventId(Long paymentEventId);
}
