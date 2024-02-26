package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuancePeriodType implements CouponUtils.CommonFieldInterface{

    CIP_1("CIP_1", "지정 기간 설정"),
    CIP_2("CIP_2", "지정 기간 없음 (지속적인 기준)"),
    CIP_3("CIP_3", "기간내 N 회 이상 주문시");

    private final String code;
    private final String value;
}
