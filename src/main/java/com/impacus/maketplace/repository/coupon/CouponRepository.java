package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findWriteLockById(Long id);
}
