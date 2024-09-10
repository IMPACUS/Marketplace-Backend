package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCouponIdDTO {

    @NotNull(message = "userCouponId는 필수 값입니다.")
    private Long userCouponId;
}
