package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIdListDTO {

    @NotEmpty
    private List<Long> couponIdList;
}
