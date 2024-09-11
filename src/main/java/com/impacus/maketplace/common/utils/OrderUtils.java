package com.impacus.maketplace.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Component
public class OrderUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static String generatePaymentKey(CheckoutSingleDTO checkoutSingleDTO) throws JsonProcessingException {
        // 객체를 JSON 문자열로 직렬화
        String serializedData = objectMapper.writeValueAsString(checkoutSingleDTO);
        // JSON 문자열의 바이트 배열을 사용하여 UUID 생성
        byte[] bytes = serializedData.getBytes(StandardCharsets.UTF_8);
        UUID paymentKey = UUID.nameUUIDFromBytes(bytes);
        return paymentKey.toString();
    }

    public static String generateOrderName(String mainName, Long count, Integer totalOrderItemCount) {
        return String.format("%s %d개", mainName, count) + (totalOrderItemCount != 1 ? String.format(" 포함 총 %d건", totalOrderItemCount) : "");
    }
}
