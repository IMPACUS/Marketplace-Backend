package com.impacus.maketplace.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class OrderUtils {

    // 허용할 문자 (숫자 0, 알파벳 o, i, j, u, v를 제외한 30개의 문자)
    private static final String allowedChars = "123456789ABCDEFGHKLMNPQRSTWXYZ";

    /**
     * 주문 번호 생성기
     * 날짜(YYMMDD) + 5개의 랜덤한 숫자 및 문자열
     */
    public static String generateOrderNumber() {
        // 현재 날짜를 YYMMDD 형태로 포맷팅
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // Random 객체 생성
        Random random = new Random();

        // 5개의 랜덤 문자 생성
        StringBuilder randomChars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(allowedChars.length());
            randomChars.append(allowedChars.charAt(index));
        }

        // 최종 주문 번호 생성
        return date + randomChars.toString();
    }
}
