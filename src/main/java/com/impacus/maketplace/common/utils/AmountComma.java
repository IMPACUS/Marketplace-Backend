package com.impacus.maketplace.common.utils;

import java.text.NumberFormat;

public class AmountComma {
    public static String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(amount);
    }
}
