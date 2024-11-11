package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.CouponProductType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ValidateUserCouponForProductDTO {
    private Long userCouponId;
    private BenefitType benefitType;  // 혜택 구분 [ 원, % ]
    private Long benefitValue;   // 혜택 금액 혹은 퍼센트
    private CouponProductType productType;    //  상품 적용 타입 [ ECO할인/그린태그 , 일반 상품, 구분안함(둘다 적용) ]
    private CoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]
    private String useCoverageSubCategoryName;  // 쿠폰 사용 범위 2차 카테고리 이름(현재는 브랜드명)
    private StandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private Long useStandardValue;  // N원 (N원 이상 주문시 사용 가능)

    @QueryProjection
    public ValidateUserCouponForProductDTO(Long userCouponId, BenefitType benefitType, Long benefitValue, CouponProductType productType, CoverageType useCoverageType, String useCoverageSubCategoryName, StandardType useStandardType, Long useStandardValue) {
        this.userCouponId = userCouponId;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.productType = productType;
        this.useCoverageType = useCoverageType;
        this.useCoverageSubCategoryName = useCoverageSubCategoryName;
        this.useStandardType = useStandardType;
        this.useStandardValue = useStandardValue;
    }

    public ValidateUserCouponForProductDTO(UserCouponInfoForCheckoutDTO userCouponInfoForCheckoutDTO) {
        this.userCouponId = userCouponInfoForCheckoutDTO.getUserCouponId();
        this.productType = userCouponInfoForCheckoutDTO.getProductType();
        this.useCoverageType = userCouponInfoForCheckoutDTO.getUseCoverageType();
        this.useCoverageSubCategoryName = userCouponInfoForCheckoutDTO.getUseCoverageSubCategoryName();
        this.useStandardType = userCouponInfoForCheckoutDTO.getUseStandardType();
        this.useStandardValue = userCouponInfoForCheckoutDTO.getUseStandardValue();
    }
}
