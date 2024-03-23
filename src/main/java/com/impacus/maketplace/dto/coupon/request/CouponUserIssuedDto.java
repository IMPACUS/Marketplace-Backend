package com.impacus.maketplace.dto.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserIssuedDto {

    private Long userId;                          //    유저 아이디
    private Boolean sendEmail;                    //    이메일알림 방식
    private Boolean sendCacaoTalk;                //    카카오알림 방식
    private Boolean sendSms;                      //    문장알림 방식
    private Long couponId;                        //    쿠폰 아이디
    private String description;                   //    쿠폰 설명
    private String couponIssuedTime;              //    발급 시점
    private String expiredAt;                     //    사용 기간
    private String couponUsableStandardAmount;    //    쿠폰 사용가능 기준 금액
    private String couponUseCoverage;             //    쿠폰 사용 범위

}
