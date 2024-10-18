package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponHistoryDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CouponApiRepository {

    List<CouponNameDTO> getCouponNames();

    List<AlarmCouponDTO> getAlarmCoupons();

    List<IssueCouponHistoryDTO> findIssueCouponHistories(String name, UserCouponStatus userCouponStatus, LocalDate startAt, LocalDate endAt);
}
