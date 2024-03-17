package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;

public interface CouponCustomRepository {

    CouponUserInfoResponse findByAddCouponInfo(CouponUserInfoRequest request);
}
