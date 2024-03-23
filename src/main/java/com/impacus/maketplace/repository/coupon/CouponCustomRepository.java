package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.dto.coupon.request.CouponSearchDto;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponCustomRepository {

    CouponUserInfoResponse findByAddCouponInfo(CouponUserInfoRequest request);

    Page<CouponListDto> findAllCouponList(CouponSearchDto couponSearchDto, Pageable pageable);
}
