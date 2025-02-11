package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.PaymentEventCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PaymentEventCouponRepository extends JpaRepository<PaymentEventCoupon, Long> {

    List<PaymentEventCoupon> findByPaymentEventId(Long paymentEventId);

    @Query("select pec.id from PaymentEventCoupon pec where pec.paymentEventId in (:paymentEventIds)")
    Set<Long> findIdByPaymentEventIdIn(List<Long> paymentEventIds);
}
