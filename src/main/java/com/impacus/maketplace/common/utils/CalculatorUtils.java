package com.impacus.maketplace.common.utils;

import org.springframework.stereotype.Component;

@Component
public class CalculatorUtils {
    public static double calculateDiscountRate(int appSalePrice, int discountPrice) {
        return ((double) discountPrice * 100) / (double) appSalePrice;
    }
}
