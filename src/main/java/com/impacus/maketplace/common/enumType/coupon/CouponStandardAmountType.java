package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStandardAmountType {

    CUSA_1("CSA_1", "가격 제한 없음"),
    CUSA_2("CSA_2", "N원 이상 구매시");

    private final String code;
    private final String value;
}
