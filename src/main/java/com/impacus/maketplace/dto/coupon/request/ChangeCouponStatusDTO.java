package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChangeCouponStatusDTO {
    @ValidEnum(enumClass = CouponStatusType.class)
    private CouponStatusType status;
}
