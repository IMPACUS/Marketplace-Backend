package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.PeriodType;
import com.impacus.maketplace.common.enumType.coupon.ProductType;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BrandCouponOverviewDTO {
    private Long couponId;
    private String name;
    private String description;
    private BenefitType benefitType;
    private Long benefitValue;
    private PeriodType periodType;
    private LocalDate periodStartAt;    // 기간 설정 시작 일자
    private LocalDate periodEndAt;  // 기간 설정 종료 일자
    private ProductType productType;    //  상품 적용 타입 [ ECO할인/그린태그 , 일반 상품, 구분안함(둘다 적용) ]
    private CoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]
    private String useCoverageSubCategoryName;  // 쿠폰 사용 범위 2차 카테고리 이름(현재는 브랜드명)

    @QueryProjection
    public BrandCouponOverviewDTO(Long couponId, String name, String description, BenefitType benefitType, Long benefitValue, PeriodType periodType, LocalDate periodStartAt, LocalDate periodEndAt, ProductType productType, CoverageType useCoverageType, String useCoverageSubCategoryName) {
        this.couponId = couponId;
        this.name = name;
        this.description = description;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.periodType = periodType;
        this.periodStartAt = periodStartAt;
        this.periodEndAt = periodEndAt;
        this.productType = productType;
        this.useCoverageType = useCoverageType;
        this.useCoverageSubCategoryName = useCoverageSubCategoryName;
    }
}
