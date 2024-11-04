package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponHistoryDTO;

import java.util.List;

public interface CouponApiRepository {

    List<CouponNameDTO> getCouponNames();

    List<AlarmCouponDTO> getAlarmCoupons();

    List<IssueCouponHistoryDTO> findIssueCouponHistories(IdsDTO dto);
}
