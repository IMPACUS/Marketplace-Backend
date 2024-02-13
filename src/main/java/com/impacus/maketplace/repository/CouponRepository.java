package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
