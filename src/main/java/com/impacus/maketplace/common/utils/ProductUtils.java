package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import org.springframework.stereotype.Component;

@Component
public class ProductUtils {
    public static double calculateDiscountRate(int appSalePrice, int discountPrice) {
        return ((double) discountPrice * 100) / (double) appSalePrice;
    }

    public static boolean checkIsFreeShipping(
            int deliveryFee,
            DeliveryRefundType deliveryFeeType,
            Integer sellerDeliveryFee
    ) {
        if (deliveryFeeType == DeliveryRefundType.FREE_SHIPPING) {
            return true;
        } else if (deliveryFeeType == DeliveryRefundType.STORE_DEFAULT) {
            return sellerDeliveryFee == 0;
        } else {
            return deliveryFee == 0;
        }
    }

    public static Integer getProductDeliveryFee(
            int deliveryFee,
            DeliveryRefundType deliveryFeeType,
            Integer sellerDeliveryFee
    ) {
        if (deliveryFeeType == DeliveryRefundType.FREE_SHIPPING) {
            return 0;
        } else if (deliveryFeeType == DeliveryRefundType.STORE_DEFAULT) {
            return sellerDeliveryFee;
        } else {
            return deliveryFee;
        }
    }
}
