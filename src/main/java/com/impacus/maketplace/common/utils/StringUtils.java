package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Component
public class StringUtils {

    public static Boolean checkPasswordValidation(String password) {
        return password.matches(RegExpPatternConstants.PASSWORD_PATTERN);
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

    public static String convertNumberFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // 문자열을 숫자로 파싱하여 형식 지정 적용 후 다시 문자열로 변환
        return decimalFormat.format(Integer.parseInt(number));

    }
    public static boolean isNotBlank(String param) {

        if (param != null) {
            param = param.trim();
            if (param.equals("") && param.length() != 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 대소문자 상관없이 keyword가 존재하는지 확인하는 함수
     *
     * @param str     keyword가 존재하는지 확인하려는 문자열
     * @param keyword
     * @return
     */
    public static boolean containsKeywordIgnoreCase(String str, String keyword) {
        return str.toLowerCase().contains(keyword.toLowerCase());
    }

}
