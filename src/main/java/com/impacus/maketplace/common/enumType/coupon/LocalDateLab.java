package com.impacus.maketplace.common.enumType.coupon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateLab {
    public static void main(String[] args) {

        LocalDateTime test = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        System.out.println(test);
    }
}
