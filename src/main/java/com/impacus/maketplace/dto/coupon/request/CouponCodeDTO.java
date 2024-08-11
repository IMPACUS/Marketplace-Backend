package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CouponCodeDTO {
    @NotBlank
    @Length(min = 10, max = 10, message = "쿠폰 코드는 10글자입니다.")
    private String couponCode;
}
