package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssueType implements CouponUtils.CommonFieldInterface {

    ONETIME("ONETIME", "일회성"),
    PERSISTENCE("PERSISTENCE", "지속성");

    private final String code;
    private final String value;

}
