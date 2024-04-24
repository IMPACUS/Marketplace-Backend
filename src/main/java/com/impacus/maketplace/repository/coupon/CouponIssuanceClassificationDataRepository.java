package com.impacus.maketplace.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssuanceClassificationDataRepository extends JpaRepository<CouponIssuanceClassificationData, Long> {
    CouponIssuanceClassificationData findByType(String type);
}
