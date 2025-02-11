package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.PaymentOrderCoupon;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentOrderCouponRepository extends JpaRepository<PaymentOrderCoupon, Long> {

    List<PaymentOrderCoupon> findByPaymentOrderIdIn(List<Long> paymentOrderIds);

    @Query("select poc.id from PaymentOrderCoupon poc where poc.id in (:paymentOrderIds)")
    List<Long> findIdByPaymentOrderIdIn(List<Long> paymentOrderIds);
}
