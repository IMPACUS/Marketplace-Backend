package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.model.ValidateOrderCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.model.ValidateProductCouponInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 결제와 관련된 로직에서 발생하는 쿠폰 유효성 검증
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCouponValidator {

    public boolean validateCouponForOrder(ValidateOrderCouponInfoDTO coupon, Long totalPrice) {
        // 타입 체크 (상품 구분: ALL)
        if (coupon.getProductType() != CouponProductType.ALL) {
            return false;
        }

        // 타입 체크 (상품 적용: 모든 브랜드)
        if (coupon.getUseCoverageType() != CoverageType.ALL) {
            return false;
        }

        // 금액 체크
        return (coupon.getUseStandardType() == StandardType.UNLIMITED && totalPrice >= 1500)
                || (coupon.getUseStandardType() == StandardType.LIMIT && totalPrice >= coupon.getUseStandardValue());
    }

    public void validateCouponForOrderWithException(ValidateOrderCouponInfoDTO coupon, Long totalPrice) {
        // 타입 체크 (상품 구분: ALL)
        if (coupon.getProductType() != CouponProductType.ALL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 타입 체크 (상품 적용: 모든 브랜드)
        if (coupon.getUseCoverageType() != CoverageType.ALL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_COVERAGE_MISMATCH);
        }

        // 금액 체크
        if ((coupon.getUseStandardType() == StandardType.UNLIMITED && totalPrice < 1500)
                || (coupon.getUseStandardType() == StandardType.LIMIT && totalPrice < coupon.getUseStandardValue())) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_STANDARD_MISMATCH);
        }
    }

    public boolean validateCouponForProduct(ValidateProductCouponInfoDTO coupon, ProductType productType, String marketName, Long appSalesPrice, Long quantity) {
        // 타입 체크 (쿠폰: 에코 적용, 상품: 그린 태그가 아니면)
        if (coupon.getProductType() == CouponProductType.ECO_GREEN && productType != ProductType.GREEN_TAG) {
            return false;
        }

        // 타입 체크 (쿠폰: 일반 상품, 상품: 일반 상품 아니면)
        if (coupon.getProductType() == CouponProductType.BASIC && productType != ProductType.GENERAL) {
            return false;
        }

        // 브랜드 체크
        if (coupon.getUseCoverageType() == CoverageType.BRAND && !Objects.equals(marketName, coupon.getUseCoverageSubCategoryName())) {
            return false;
        }

        // 금액 체크
        long totalProductPrice = appSalesPrice * quantity;
        return (coupon.getUseStandardType() == StandardType.UNLIMITED && totalProductPrice >= 1500)
                || (coupon.getUseStandardType() == StandardType.LIMIT && totalProductPrice >= coupon.getUseStandardValue());
    }

    public void validateCouponForProductWithException(ValidateProductCouponInfoDTO coupon, ProductType productType, String marketName, Long appSalesPrice, Long quantity) {
        // 타입 체크 (쿠폰: 에코 적용, 상품: 그린 태그가 아니면)
        if (coupon.getProductType() == CouponProductType.ECO_GREEN && productType != ProductType.GREEN_TAG) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 타입 체크 (쿠폰: 일반 상품, 상품: 일반 상품 아니면)
        if (coupon.getProductType() == CouponProductType.BASIC && productType != ProductType.GENERAL) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_TYPE_MISMATCH);
        }

        // 브랜드 체크
        if (coupon.getUseCoverageType() == CoverageType.BRAND && !Objects.equals(marketName, coupon.getUseCoverageSubCategoryName())) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_COVERAGE_MISMATCH);
        }

        // 금액 체크
        long totalProductPrice = appSalesPrice * quantity;
        if ((coupon.getUseStandardType() == StandardType.UNLIMITED && totalProductPrice < 1500)
                || (coupon.getUseStandardType() == StandardType.LIMIT && totalProductPrice < coupon.getUseStandardValue())) {
            throw new CustomException(CouponErrorType.INVALID_USER_COUPON_USE_STANDARD_MISMATCH);
        }
    }
}
