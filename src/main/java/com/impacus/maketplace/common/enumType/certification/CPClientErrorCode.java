package com.impacus.maketplace.common.enumType.certification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CPClientErrorCode {
    DECRYPTION_SYSTEM_ERROR(-1, "복호화 시스템 오류입니다."),
    DECRYPTION_PROCESSING_ERROR(-4, "복호화 처리 오류입니다."),
    DECRYPTION_HASH_ERROR(-5, "복호화 해쉬 오류입니다."),
    DECRYPTION_DATA_ERROR(-6, "복호화 데이터 오류입니다."),
    INPUT_DATA_ERROR(-9, "입력 데이터 오류입니다."),
    SITE_PASSWORD_ERROR(-12, "사이트 패스워드 오류입니다."),
    UNKNOWN_ERROR(0, "알 수 없는 에러입니다."); // 기본값

    private final int code;
    private final String message;

    public static CPClientErrorCode fromCode(int code) {
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.code == code)
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}
