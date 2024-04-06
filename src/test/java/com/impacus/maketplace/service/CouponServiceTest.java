package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponIssuedDto;
import com.impacus.maketplace.dto.coupon.request.CouponUpdateDto;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.CouponIssuanceClassificationDataRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponIssuanceClassificationDataRepository couponIssuanceClassificationDataRepository;

    @Autowired
    private CouponAdminService couponAdminService;

    @Autowired
    private CouponRepository couponRepository;

    // 구분 상세 데이터 insert
//    @Test
//    void registerCouponIssuanceClassificationData() {
//        String[] cic1Arr = {
//                "7,000원 이상 제품 구매시 지급",
//                "10,000원 이상 제품 구매시 지급",
//                "15,000원 이상 제품 구매시 지급",
//                "20,000원 이상 제품 구매시 지급",
//                "25,000원 이상 제품 구매시 지급",
//                "50,000원 이상 제품 구매시 지급",
//                "60,000원 이상 제품 구매시 지급",
//                "70,000원 이상 제품 구매시 지급",
//                "80,000원 이상 제품 구매시 지급",
//                "10만원 이상 제품 구매시 지급",
//                "15만원 이상 제품 구매시 지급",
//                "20만원 이상 제품 구매시 지급",
//                "25만원 이상 제품 구매시 지급",
//                "30만원 이상 제품 구매시 지급",
//        };
//
//        String[] cic2Arr = {
//                "주 1회 각 20,000원 이상 주문시 지급",
//                "주 2회 각 15,000원 이상 주문시 지급",
//                "주 3회 각 10,000원 이상 주문시 지급",
//                "주 4회 각 25,000원 이상 주문시 지급",
//                "주 5회 각 30,000원 이상 주문시 지급",
//                "주 6회 각 20,000원 이상 주문시 지급",
//                "월 5회 각 20,000원 이상 주문시 지급",
//                "월 10회 각 30,000원 이상 주문시 지급",
//                "월 15회 각 25,000원 이상 주문시 지급",
//        };
//
//        for (String c1 : cic1Arr) {
//            CouponIssuanceClassificationData data = CouponIssuanceClassificationData.builder()
//                    .title(c1)
//                    .type(CouponIssuanceClassification.CIC_1)
//                    .build();
//            CouponIssuanceClassificationData save = couponIssuanceClassificationDataRepository.save(data);
//        }
//
//        for (String c2 : cic2Arr) {
//            CouponIssuanceClassificationData data = CouponIssuanceClassificationData.builder()
//                    .title(c2)
//                    .type(CouponIssuanceClassification.CIC_2)
//                    .build();
//            CouponIssuanceClassificationData save = couponIssuanceClassificationDataRepository.save(data);
//        }
//    }

    @Test
    void addCouponForAdmin() {

        CouponIssuedDto newCoupon = CouponIssuedDto.builder()
                .name("테스트 쿠퐆 V2")
                .desc("이것은 테스트 쿠폰V2 입니다.")
                .couponBenefitClassificationType("amount") // 원
                .benefitAmount(15000)
                .couponIssuanceClassificationType("CIC_2") //유저 일반
                .couponIssuanceClassificationData(15L)
                .couponPaymentTargetType("CPT_1") // 모든 회원
                .firstComeFirstServedAmount(null)
                .couponIssuedTimeType("CIT_2") // 즉시 발급
                .couponExpireTimeType("CET_1") // 발급일로 부터 N일
                .couponType("CT_2")
                .expireDays(7L)
                .numberOfWithPeriod(3L)
                .couponIssuanceCoverageType("CC_1") // 모든 브랜드
                .couponUseCoverageType("CC_1")  // 모든 브랜드
                .couponUsableStandardAmountType("CSA_2") // N원 이상 구매시 사용 가능
                .usableStandardMount(30000)
                .couponIssuanceStandardAmount("CSA_2") // N원 이상 구매 쿠폰 발급
                .issueStandardMount(10000)
                .couponIssuancePeriodType("CIP_1")  // 지정 기간 설정
                .startIssuanceAt("2024-07-01")
                .endIssuanceAt("2024-09-30")
                .couponIssuanceType("CIT_1") // 자동 발급
                .loginCouponIssueNotification("Y")
                .build();

        Boolean result = couponAdminService.addCoupon(newCoupon);
        System.out.println(result);

    }

    @Test
    @Rollback(value = false)
    void updateTest() {
        CouponUpdateDto updateDto = CouponUpdateDto.builder()
                .couponBenefitClassificationType("percent").build();
        couponAdminService.updateCouponDetail(updateDto);
    }

    @Test
    void showTest() {
        Coupon coupon = couponRepository.findById(1L).get();
        System.out.println(coupon);
    }

    @Test
    void generateCoupon() {
        String s = CouponUtils.generateCode();
        System.out.println(s);
        System.out.println("ss " +  s.length());
        String test = "WWJWP46JHG8SOYPPXT9FY2JTX4I2OLOD8YPQ";
        System.out.println(test.length());


    }
}
