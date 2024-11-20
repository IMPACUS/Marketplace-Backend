package com.impacus.maketplace.common.enumType.certification;

import lombok.Getter;

@Getter
public enum CerticationEncryptionErrorCode {
    SYSTEM_ERROR(-1, "암호화 시스템 에러입니다."),
    PROCESSING_ERROR(-2, "암호화 처리 오류입니다."),
    DATA_ERROR(-3, "암호화 데이터 오류입니다."),
    INPUT_ERROR(-9, "입력 데이터 오류입니다."),
    UNKNOWN_ERROR(0, "알 수 없는 에러입니다.");

    private final int code;
    private final String message;

    CerticationEncryptionErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CerticationEncryptionErrorCode fromCode(int code) {
        for (CerticationEncryptionErrorCode errorCode : CerticationEncryptionErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR; // 정의되지 않은 코드일 경우
    }
}