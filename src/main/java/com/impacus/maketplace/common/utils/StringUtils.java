package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Component
public class StringUtils {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    public static Boolean checkPasswordValidation(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public static String createStrEmail(String email, OauthProviderType oauthProviderType) {
        return oauthProviderType + "_" + email;
    }

    public static String parseGrantTypeInToken(String strGrantType, String token) {
        if (token.startsWith(strGrantType + " ")) {
            return token.substring(strGrantType.length() + 1);
        }

        return token;
    }

    /**
     * 파일명에서 파일 확장자를 찾는 함수
     *
     * @param filename
     * @return
     */
    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    /**
     * YYMMddHHmmXXX 형식의 상품번호를 생성하는 함 (XXX: 랜덤 숫자)
     *
     * @return
     */
    public static String getProductNumber() {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        Random random = new Random(Long.parseLong(nowDate));
        int randomNumber = random.nextInt(999 - 100 + 1) + 100;
        
        return nowDate + randomNumber;
    }

    /**
     * String 타입의 number 가 들어오면 구분[,] 넣어주기
     * ex ) String number = 4000;
     *
     * @return 4,000
     */

    public static String updateNumberFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // 문자열을 숫자로 파싱하여 형식 지정 적용 후 다시 문자열로 변환
        return decimalFormat.format(Integer.parseInt(number));

    }

}
