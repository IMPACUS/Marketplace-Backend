package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponExpireTime implements CouponUtils.CommonFieldInterface{

    CET_1("CET_1", "발급일로 부터 N 일 이내"),
    CET_2("CET_2", "기간제한 없음");

    private final String code;
    private final String value;
}
