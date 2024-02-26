package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponExpireTime {

    CET_1("CET_1", "발급일로 부터 N 일"),
    CET_2("CET_2", "기간제한 없음(무제한)");

    private final String code;
    private final String value;
}
