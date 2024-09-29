package com.impacus.maketplace.dto.user.request;

import com.impacus.maketplace.common.annotation.ValidBirthDate;
import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import com.impacus.maketplace.dto.paymentMethod.request.CreatePaymentMethodDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    @NotBlank
    @ValidPhoneNumber
    private String phoneNumber;

    @NotBlank
    @ValidBirthDate
    private String birthDate;

    @NotNull
    private List<CreatePaymentMethodDTO> paymentMethods;

    private String name;
}
