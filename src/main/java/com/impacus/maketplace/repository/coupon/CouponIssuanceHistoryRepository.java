package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.CouponIssuanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssuanceHistoryRepository extends JpaRepository<CouponIssuanceHistory, Long> {
}
