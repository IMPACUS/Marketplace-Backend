package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
