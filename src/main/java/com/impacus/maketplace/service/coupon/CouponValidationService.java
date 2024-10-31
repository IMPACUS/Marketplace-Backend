package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.TargetProductType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.model.ValidateOrderCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.model.ValidateProductCouponInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponValidationService {

    public boolean validateCouponForOrder(ValidateOrderCouponInfoDTO coupon, Long totalPrice) {
        // 타입 체크 (상품 구분: ALL)
        if (coupon.getProductType() != TargetProductType.ALL) {
            return false;
        }

        // 타입 체크 (상품 적용: 모든 브랜드)
        if (coupon.getUseCoverageType() != CoverageType.ALL) {
            return false;
        }

        // 금액 체크
        return coupon.getUseStandardType() != StandardType.LIMIT || coupon.getUseStandardValue() <= totalPrice;
    }

    public void validateCouponForOrderWithException(ValidateOrderCouponInfoDTO coupon, Long totalPrice) {
        // 타입 체크 (상품 구분: ALL)
        if (coupon.getProductType() != TargetProductType.ALL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 타입 체크 (상품 적용: 모든 브랜드)
        if (coupon.getUseCoverageType() != CoverageType.ALL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 금액 체크
        if (coupon.getUseStandardType() == StandardType.LIMIT && coupon.getUseStandardValue() > totalPrice) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_STANDARD_MISMATCH);
        }
    }

    public boolean validateCouponForProduct(ValidateProductCouponInfoDTO coupon, ProductType productType, String marketName, Long appSalesPrice, Long quantity) {
        // 타입 체크 (쿠폰: 에코 적용, 상품: 그린 태그가 아니면)
        if (coupon.getProductType() == TargetProductType.ECO_GREEN && productType != ProductType.GREEN_TAG) {
            return false;
        }

        // 타입 체크 (쿠폰: 일반 상품, 상품: 일반 상품 아니면)
        if (coupon.getProductType() == TargetProductType.BASIC && productType != ProductType.GENERAL) {
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

    public void validateCouponForProductWithException(ValidateProductCouponInfoDTO coupon, ProductType productType, String marketName, Long appSalesPrice, Long quantity) {
        // 타입 체크 (쿠폰: 에코 적용, 상품: 그린 태그가 아니면)
        if (coupon.getProductType() == TargetProductType.ECO_GREEN && productType != ProductType.GREEN_TAG) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 타입 체크 (쿠폰: 일반 상품, 상품: 일반 상품 아니면)
        if (coupon.getProductType() == TargetProductType.BASIC && productType != ProductType.GENERAL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 브랜드 체크
        if (coupon.getUseCoverageType() == CoverageType.BRAND && !Objects.equals(marketName, coupon.getUseCoverageSubCategoryName())) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_COVERAGE_MISMATCH);
        }

        // 금액 체크
        Long totalProductPrice = appSalesPrice * quantity;
        if (coupon.getUseStandardType() == StandardType.LIMIT && coupon.getUseStandardValue() > totalProductPrice) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_STANDARD_MISMATCH);
        }
    }
}
