package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueCouponTargetUserDTO {

    @NotNull(message = "사용자의 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "쿠폰 ID는 필수입니다.")
    private Long couponId;
}
