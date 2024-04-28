package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponUserListDto {

    // 화면에 보여줄 문구
    private Long id;
    private String price;   // ex) 20,000원
    private String name;    // ex) 환경을 위한 감사 쿠폰
    private String desc;    // ex) 13만원 이상의 제품 구매시 사용 가능
    private String expireDate;  //ex) 24.05.24 23:59까지
    private Boolean isDownloaded;
    private String availableDownloadAt; //  다운로드 가능한 일자
    private Boolean couponLock;  //  다운 불가능한 쿠폰 잠금
    private String buttonValue;
    private String createAt;
    private CouponType type;              // 쿠폰 형식 [ 이벤트 , 지급형 ]


    //    // 쿼리에서 가져올 정보
//    private Long id;    //  쿠폰Í 아이디
//    private String couponName;  //  쿠폰 이름
    private CouponBenefitType benefitType;    // 혜택 구분 [ 원, % ]
    private int benefitValue;   // 혜택 금액 및 퍼센트
    private CouponPaymentTargetType paymentTargetType;    // 지급 대상 [ 모든 회원, 선착순 ]
    private Long firstCount = 0L; // 선착순 발급 수
    private CouponExpireTimeType expireTimeType;  // 사용기간 [ 발급일로 부터 N일, 무제한 ]
//    private CouponCoverage couponIssuanceCoverage;  // 발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
//    private CouponCoverage couponUseCoverage;   // 쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
    private CouponStandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private int useStandardValue = 0; // N원 (N원 이상 주문시 사용 가능)



}
