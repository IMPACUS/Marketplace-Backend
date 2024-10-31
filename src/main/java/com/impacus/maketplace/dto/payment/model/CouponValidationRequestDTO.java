package com.impacus.maketplace.dto.payment.model;

import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class CouponValidationRequestDTO {

    private List<ProductCouponValidationData> productInfosForCoupon;
    private List<Long> orderCouponIds;
    private Long orderTotalPrice;

    @Getter
    @NoArgsConstructor
    public static class ProductCouponValidationData {
        private Long productId;
        private List<Long> appliedCouponIds;
        private Long productPrice;
        private Long quantity;
        private ProductType productType;
        private String marketName;

        public ProductCouponValidationData(Long productId, List<Long> appliedCouponIds, int productPrice, Long quantity, ProductType productType, String marketName) {
            this.productId = productId;
            this.appliedCouponIds = appliedCouponIds;
            this.productPrice = (long) productPrice;
            this.quantity = quantity;
            this.productType = productType;
            this.marketName = marketName;
        }

        public int getAppliedCouponCount() {
            return this.appliedCouponIds.size();
        }
    }

    public static CouponValidationRequestDTO createEmptyValidatedCouponDTO(Long orderTotalPrice) {
        return new CouponValidationRequestDTO(Collections.emptyList(), Collections.emptyList(), orderTotalPrice);
    }

    public boolean isEmpty() {
        return productInfosForCoupon.isEmpty() && orderCouponIds.isEmpty();
    }

    public List<Long> getUserCouponIds() {
        List<Long> userCouponIds = new ArrayList<>();
        productInfosForCoupon.forEach(productCoupon -> userCouponIds.addAll(productCoupon.getAppliedCouponIds()));
        userCouponIds.addAll(userCouponIds);
        return userCouponIds;
    }

    public int getUserCouponCount() {
        int count = productInfosForCoupon.stream().mapToInt(ProductCouponValidationData::getAppliedCouponCount).sum();
        count += orderCouponIds.size();

        return count;
    }
}
