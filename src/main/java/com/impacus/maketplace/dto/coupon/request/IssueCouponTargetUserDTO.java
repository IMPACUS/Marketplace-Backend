package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueCouponTargetUserDTO {

    @NotBlank(message = "지급할 사용자의 이메일이 들어오지 않았습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;

    @NotNull(message = "쿠폰 ID는 필수입니다.")
    private Long couponId;
}
