package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CouponStatusDTO {

    @NotNull
    private List<Long> couponIdList;

    @ValidEnum(enumClass = CouponStatusType.class)
    private CouponStatusType changeStatus;
}
