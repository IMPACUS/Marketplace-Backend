package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;

@Slf4j
public class CouponUtils {

    /**
     * Code 생성기
     * 기능 추가: DB를 통해 중복 검사
     * 추후 기능 확장 예정 (미리 여러 코드 생성해 놓은 뒤 전부 등록해놓고 사용)
     */
    public static String generateCode() {
        final int CODE_LENGTH = 10;

        final char[] charTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random(System.currentTimeMillis());

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < CODE_LENGTH; i++) {
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
