package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findWriteLockById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id IN :couponIdList")
    List<Coupon> findWriteLockCouponsById(List<Long> couponIdList);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findWriteLockCouponByCode(String code);

    Optional<Coupon> findByCode(String code);

    @Query("SELECT c FROM Coupon c WHERE c.isDeleted = false AND c.statusType <> 'STOP'")
    List<Coupon> findAllActiveCoupons();
}
