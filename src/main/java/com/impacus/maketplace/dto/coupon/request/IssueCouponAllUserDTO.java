package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class IssueCouponAllUserDTO {

    private UserLevel userLevel;

    @NotNull(message = "쿠폰 ID는 필수입니다.")
    private Long couponId;
}
