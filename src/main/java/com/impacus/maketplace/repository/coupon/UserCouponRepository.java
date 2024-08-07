package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    Optional<UserCoupon> findByIdAndUserId(Long id, Long userId);
}
