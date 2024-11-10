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
public class ValidateOrderCouponInfoDTO {
    private CouponProductType productType;    //  상품 적용 타입 [ ECO할인/그린태그 , 일반 상품, 구분안함(둘다 적용) ]
    private CoverageType useCoverageType;   // 쿠폰 사용 범위 [ 모든 상품/특정 브랜드 및 카테고리 ]
    private StandardType useStandardType;    // 쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
    private Long useStandardValue;  // N원 (N원 이상 주문시 사용 가능)

    public static ValidateOrderCouponInfoDTO fromDto(UserCouponInfoForCheckoutDTO dto) {
        return new ValidateOrderCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseStandardType(), dto.getUseStandardValue());
    }

    public static ValidateOrderCouponInfoDTO fromDto(PaymentUserCouponInfo dto) {
        return new ValidateOrderCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseStandardType(), dto.getUseStandardValue());
    }
}
