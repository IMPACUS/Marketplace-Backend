package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponCustomRepositroy {
Page<CouponListInfoDTO> findCouponListInfo(String name, CouponStatusType couponStatus, Pageable pageable);
}
