package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponCustomRepository {

    Optional<Coupon> findByCode(String code);


}
