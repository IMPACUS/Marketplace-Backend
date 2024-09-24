package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.dto.coupon.response.*;
import com.impacus.maketplace.repository.coupon.querydsl.dto.UserCouponInfoForCheckoutDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CouponCustomRepositroy {
    Page<CouponListInfoDTO> findCouponListInfo(String name, CouponStatusType couponStatus, Pageable pageable);

    List<IssueCouponInfoDTO> findIssueCouponInfoList();

    Page<IssueCouponHIstoryDTO> findIssueCouponHistoryList(String name, UserCouponStatus UserCouponStatus, LocalDate startAt, LocalDate endAt, Pageable pageable);

    List<UserCouponOverviewDTO> findUserCouponOverviewList(Long userId);

    List<BrandCouponOverviewDTO> findBrandCouponList(Long userId, String brandName, Boolean isEcoProduct);

    List<UserCouponInfoForCheckoutDTO> findUserCouponInfoForCheckoutList(Long userId);
}
