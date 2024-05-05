package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserIssuedDto {

    @NotBlank
    private String couponTarget;                  //    지급 대상 CouponTargetType[회원검색, 모든회원]

    private List<String> userLevelList = new ArrayList<>(); // 등급 대상 UserLevel

    private Long userId;                          //    유저 아이디

    @NotNull
    private List<String> alarmTypeList = new ArrayList<>();     //    지급 알림 방식

    @NotNull
    private Long couponId;                        //    쿠폰 아이디

}
