package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.coupon.CouponIssuanceClassification;
import com.impacus.maketplace.entity.coupon.CouponIssuanceClassificationData;
import com.impacus.maketplace.repository.coupon.CouponIssuanceClassificationDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponIssuanceClassificationDataRepository couponIssuanceClassificationDataRepository;


    @Test
    void registerCouponIssuanceClassificationData() {
        String[] cic1Arr = {
                "7,000원 이상 제품 구매시 지급",
                "10,000원 이상 제품 구매시 지급",
                "15,000원 이상 제품 구매시 지급",
                "20,000원 이상 제품 구매시 지급",
                "25,000원 이상 제품 구매시 지급",
                "50,000원 이상 제품 구매시 지급",
                "60,000원 이상 제품 구매시 지급",
                "70,000원 이상 제품 구매시 지급",
                "80,000원 이상 제품 구매시 지급",
                "10만원 이상 제품 구매시 지급",
                "15만원 이상 제품 구매시 지급",
                "20만원 이상 제품 구매시 지급",
                "25만원 이상 제품 구매시 지급",
                "30만원 이상 제품 구매시 지급",
        };

        String[] cic2Arr = {
            "주 1회 각 20,000원 이상 주문시 지급",
            "주 2회 각 15,000원 이상 주문시 지급",
            "주 3회 각 10,000원 이상 주문시 지급",
            "주 4회 각 25,000원 이상 주문시 지급",
            "주 5회 각 30,000원 이상 주문시 지급",
            "주 6회 각 20,000원 이상 주문시 지급",
            "월 5회 각 20,000원 이상 주문시 지급",
            "월 10회 각 30,000원 이상 주문시 지급",
            "월 15회 각 25,000원 이상 주문시 지급",
        };

        for (String c1 : cic1Arr) {
            CouponIssuanceClassificationData.builder()
                    .title(c1)
                    .type(CouponIssuanceClassification.CIC_1)
                    .build();
        }

        for (String c2 : cic2Arr) {
            CouponIssuanceClassificationData.builder()
                    .title(c2)
                    .type(CouponIssuanceClassification.CIC_1)
                    .build();
        }

}}
