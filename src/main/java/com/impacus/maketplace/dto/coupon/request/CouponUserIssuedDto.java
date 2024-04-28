package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserIssuedDto {

    @NotBlank
    private String couponTarget;                  //    지급 대상 CouponTargetType[회원검색, 모든회원]
    @NotNull
    private Long userId;                          //    유저 아이디
    @NotBlank
    private String[] alarmType;                     //    지금 알림 방식
    @NotNull
    private Long couponId;                        //    쿠폰 아이디폰

}
