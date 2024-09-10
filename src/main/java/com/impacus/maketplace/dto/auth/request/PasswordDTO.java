package com.impacus.maketplace.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordDTO {
    @NotBlank
    private String password;
}
