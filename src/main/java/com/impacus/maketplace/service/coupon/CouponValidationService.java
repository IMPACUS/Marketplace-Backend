package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.ProductType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.impacus.maketplace.repository.coupon.querydsl.dto.ValidateUserCouponForOrderDTO;
import com.impacus.maketplace.repository.coupon.querydsl.dto.ValidateUserCouponForProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponValidationService {

    public boolean validateCouponForOrder(ValidateUserCouponForOrderDTO coupon, Long totalPrice) {
        // 타입 체크
        if (coupon.getProductType() != ProductType.ALL) {
            return false;
        }

        if (coupon.getUseCoverageType() != CoverageType.ALL) {
            return false;
        }

        // 금액 체크
        return coupon.getUseStandardType() != StandardType.LIMIT || coupon.getUseStandardValue() <= totalPrice;
    }
    public boolean validateCouponForProduct(ValidateUserCouponForProductDTO coupon, com.impacus.maketplace.common.enumType.product.ProductType productType, String marketName, int appSalesPrice, Long quantity) {
        // 타입 체크 (쿠폰: 에코 적용, 상품: 그린 태그가 아니면)
        if (coupon.getProductType() == ProductType.ECO_GREEN && productType != com.impacus.maketplace.common.enumType.product.ProductType.GREEN_TAG) {
            return false;
        }

        // 타입 체크 (쿠폰: 일반 상품, 상품: 일반 상품 아니면)
        if (coupon.getProductType() == ProductType.BASIC && productType != com.impacus.maketplace.common.enumType.product.ProductType.GENERAL) {
            return false;
        }

        // 브랜드 체크
        if (coupon.getUseCoverageType() == CoverageType.BRAND && !Objects.equals(marketName, coupon.getUseCoverageSubCategoryName())) {
            return false;
        }

        // 금액 체크
        Long totalProductPrice = appSalesPrice * quantity;
        return coupon.getUseStandardType() != StandardType.LIMIT || coupon.getUseStandardValue() <= totalProductPrice;
    }
}
