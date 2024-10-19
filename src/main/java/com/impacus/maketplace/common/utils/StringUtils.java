package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.dto.user.EmailInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StringUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    private static final String COUNTER_KEY = "unique_id_counter";
    private static final String DATE_KEY = "unique_id_date";

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public StringUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 고유번호 생성하는 함수
     * - 날짜가 변경될 때마다 카운터 초기화
     *
     * @return 고유번호
     */
    public static String generateUniqueNumber() {
        String currentDate = dateFormat.format(new Date());
        String storedDate = redisTemplate.opsForValue().get(DATE_KEY);

        if (!currentDate.equals(storedDate)) {
            redisTemplate.opsForValue().set(DATE_KEY, currentDate);
            redisTemplate.opsForValue().set(COUNTER_KEY, "0");
        }

        Long counter = redisTemplate.opsForValue().increment(COUNTER_KEY);
        return currentDate + String.format("%05d", counter);
    }

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
     * String 타입의 number 가 들어오면 구분[,] 넣어주기
     * ex ) String number = 4000;
     *
     * @return 4, 000
     */

    public static String convertNumberFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // 문자열을 숫자로 파싱하여 형식 지정 적용 후 다시 문자열로 변환
        return decimalFormat.format(Integer.parseInt(number));

    }

    public static boolean isNotBlank(String param) {

        if (param != null) {
            param = param.trim();
            if (param.equals("") && param.length() != 0) {
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
        if (str == null) {
            return false;
        }

        return str.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * user_info.email을 OauthProviderType와 이메일 계정을 추출해서 반환하는 함수
     *
     * @param email Format: OauthProviderKey_Email
     * @return
     */
    public static EmailInfoDTO getEmailInfo(String email) {
        String[] emailInfo = email.split("_");
        OauthProviderType oauthProviderType = OauthProviderType.valueOf(emailInfo[0]);

        return EmailInfoDTO.of(emailInfo[1], oauthProviderType);
    }

}
