package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long>, CouponCustomRepository {

    List<CouponUser> findByUser(User user);


    //TODO: 만료된 쿠폰, 사용한 쿠폰, 사용할 수 있는 쿠폰

    //TODO: 쿠폰 만료 3일전 알림 기능


}
