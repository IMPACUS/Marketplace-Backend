package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;

import java.util.List;

public interface CouponApiRepository {

    List<CouponNameDTO> getCouponNames();

    List<AlarmCouponDTO> getAlarmCoupons();
}
