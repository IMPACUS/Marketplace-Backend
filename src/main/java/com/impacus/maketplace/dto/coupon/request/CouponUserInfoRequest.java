package com.impacus.maketplace.dto.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponUserInfoRequest {

    private String provisionTarget;     // 지급 대상 [ 회원 검색 : TARGET_USER, 모든 회원 : TARGET_ALL ]
    private String userId;
    private String userName;



}
