package com.impacus.maketplace.dto.coupon.model;

import com.impacus.maketplace.entity.coupon.Coupon;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CouponConditionCheckResultDTO {
    private final boolean valid;
    private final Coupon coupon;
    private final List<Long> paymentEventId;

    public static CouponConditionCheckResultDTO fail() {
        return new CouponConditionCheckResultDTO(false, null, Collections.emptyList());
    }
}
