package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class PhoneNumberDTO {
    private String phoneNumberPrefix;
    private String phoneNumberSuffix;

    public PhoneNumberDTO(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() >= 4) {
            List<String> parts = Arrays.stream(phoneNumber.split("-")).toList();
            this.phoneNumberPrefix = String.join("-", parts.subList(0, parts.size() - 1));
            this.phoneNumberSuffix = parts.get(parts.size() - 1);
        } else {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "\"유효한 전화번호를 입력해주세요.\"");
        }
    }
}
