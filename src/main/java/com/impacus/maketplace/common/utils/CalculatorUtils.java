package com.impacus.maketplace.common.utils;

import org.springframework.stereotype.Component;

@Component
public class CalculatorUtils {
    public static float calculateDiscountRate(int appSalePrice, int discountPrice) {
        return ((float) discountPrice * 100) * (float) appSalePrice;
    }
}
