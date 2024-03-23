package com.impacus.maketplace.dto.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponSearchDto {
    private String searchCouponName;    //  쿠폰이름검색
    private String orderStatus;         //  발급중, 발급됨, 발급중지 별로 검색
    private Integer searchCount;        //  N 개씩보기
    private boolean searchNotStop;      //  발급 중지된 쿠폰은 보이지 않기위한


    @Builder.Default
    private int pageIndex = 0;

}
