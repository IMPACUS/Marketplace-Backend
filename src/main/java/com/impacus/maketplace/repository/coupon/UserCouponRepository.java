package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    Optional<UserCoupon> findByIdAndUserId(Long id, Long userId);

    @Query("select u from UserCoupon u where " +
            "u.userId = :userId " +
            "and u.isDownload = true " +
            "and u.isUsed = false " +
            "and u.status = 'ISSUE_SUCCESS'" +
            "and u.expiredAt >= CURRENT_DATE")
    List<UserCoupon> findAvailableCouponList(Long userId);
}
