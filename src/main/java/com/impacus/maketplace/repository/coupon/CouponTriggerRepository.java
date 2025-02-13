package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.CouponTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CouponTriggerRepository extends JpaRepository<CouponTrigger, Long> {

    @Query("select ct.triggerId from CouponTrigger ct where ct.userId = (:userId) and ct.triggerType = 'ORDER'")
    Set<Long> findPaymentEventId(Long userId);
}
