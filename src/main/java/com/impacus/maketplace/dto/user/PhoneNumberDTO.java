package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhoneNumberDTO {
    private String phoneNumberPrefix;
    private String phoneNumberSuffix;

    public PhoneNumberDTO(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() >= 4) {
            this.phoneNumberPrefix = phoneNumber.substring(0, phoneNumber.length() - 4);
            this.phoneNumberSuffix = phoneNumber.substring(phoneNumber.length() - 4);
        } else {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "\"유효한 전화번호를 입력해주세요.\"");
        }
    }
}
