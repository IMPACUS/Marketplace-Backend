package com.impacus.maketplace.dto.coupon.response;

import java.text.DecimalFormat;

public class Lab {
    public static void main(String[] args) {
        String amountStr = "4000";

        // 숫자 형식 지정
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        // 문자열을 숫자로 파싱하여 형식 지정 적용 후 다시 문자열로 변환
        String formattedAmount = decimalFormat.format(Integer.parseInt(amountStr));

        // 결과 출력
        System.out.println(formattedAmount);
    }
}
