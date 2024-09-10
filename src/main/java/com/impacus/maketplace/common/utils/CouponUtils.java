package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Component
public class CouponUtils {

    /**
     * Code 생성기
     * 기능 추가: DB를 통해 중복 검사
     * 추후 기능 확장 예정 (미리 여러 코드 생성해 놓은 뒤 전부 등록해놓고 사용)
     */
    public static String generateCode() {
        LocalDate today = LocalDate.now();
        final int CODE_LENGTH = 10; // 총 코드 길이

        // 년도의 마지막 두 글자
        int yearLastDigit = today.getYear() % 100;

        // 월을 나타내는 두 글자
        String[] monthCodes = {"JA", "FB", "MR", "AP", "MY", "JN", "JL", "AG", "SP", "OT", "NV", "DC"};
        String monthCode = monthCodes[today.getMonthValue() - 1];

        // 일수를 나타내는 두 글자
        String dayCode = String.format("%02d", today.getDayOfMonth());

        // 나머지 네 글자 생성을 위한 문자 테이블
        final char[] charTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random(System.nanoTime());
        StringBuffer buffer = new StringBuffer();

        // 년, 월, 일 코드를 버퍼에 추가
        buffer.append(yearLastDigit);
        buffer.append(monthCode);
        buffer.append(dayCode);

        // 나머지 네 글자를 랜덤하게 생성
        for (int i = 0; i < CODE_LENGTH - 6; i++) { // 이미 6글자를 추가했으므로, 4글자만 더 생성
            buffer.append(charTable[random.nextInt(charTable.length)]);
        }

        return buffer.toString();
    }


    /**
     * Code로부터 Enum 클래스 인스턴스 추출
     */
    public static <E extends Enum<E> & CommonFieldInterface> E fromCode(Class<E> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElseThrow(() ->{
                    log.warn("Coupon: 알 수 없는 코드가 들어왔습니다, enum class: {}, input code: {}}", enumClass.getName(), code);
                    return new CustomException(CommonErrorType.UNKNOWN);
                });
    }
    public interface CommonFieldInterface {
        String getCode();

        String getValue();
    }
}
