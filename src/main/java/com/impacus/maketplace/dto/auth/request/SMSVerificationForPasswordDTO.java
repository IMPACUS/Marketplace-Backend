package com.impacus.maketplace.dto.auth.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SMSVerificationForPasswordDTO {
    @ValidPhoneNumber
    public String phoneNumber;

    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;
    
    @NotBlank
    private String code;
}
