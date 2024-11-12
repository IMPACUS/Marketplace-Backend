package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.enumType.coupon.EventType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEventTypeDTO {
    private String code;
    private String value;
}
