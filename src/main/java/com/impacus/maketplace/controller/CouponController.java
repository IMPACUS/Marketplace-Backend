package com.impacus.maketplace.controller;

import com.impacus.maketplace.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 발급 API
     *  1. 오픈 기념 첫 회원 가입 20% 할인 쿠폰
     *  2. 선택적 일정 (n월 회원가입 이벤트 20%할인 쿠폰 증젖ㅇ)
     *  3. (_주 회원가입 이벤트 15% 할인 쿠폱 증정)
     *  4. 친구 초대 이벤트 (ex 1주일간 친구초대 5명, 친구랑 10% 할인 크푼 증정) 보류 (가능한)
     */

}
