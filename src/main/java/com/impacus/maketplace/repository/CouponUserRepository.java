package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.coupon.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {
}
