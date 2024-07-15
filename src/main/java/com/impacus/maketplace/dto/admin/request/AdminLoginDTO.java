package com.impacus.maketplace.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDTO {

    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    private String adminIdName;
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;
}
