package com.impacus.maketplace.dto.coupon.model;

import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.impacus.maketplace.common.enumType.coupon.CouponProductType;
import com.impacus.maketplace.repository.coupon.querydsl.dto.PaymentUserCouponInfo;
import com.impacus.maketplace.repository.coupon.querydsl.dto.UserCouponInfoForCheckoutDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateProductCouponInfoDTO {
    private CouponProductType productType;    //  상품 적용 타입 [ ECO할인/그린태그 , 일반 상품, 구분안함(둘다 적용) ]
    private CoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]
    private String useCoverageSubCategoryName;  // 쿠폰 사용 범위 2차 카테고리 이름(현재는 브랜드명)
    private StandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private Long useStandardValue;  // N원 (N원 이상 주문시 사용 가능)

    public static ValidateProductCouponInfoDTO fromDto(UserCouponInfoForCheckoutDTO dto) {
        return new ValidateProductCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseCoverageSubCategoryName(), dto.getUseStandardType(), dto.getUseStandardValue());
    }

    public static ValidateProductCouponInfoDTO fromDto(PaymentUserCouponInfo dto) {
        return new ValidateProductCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseCoverageSubCategoryName(), dto.getUseStandardType(), dto.getUseStandardValue());
    }
}
