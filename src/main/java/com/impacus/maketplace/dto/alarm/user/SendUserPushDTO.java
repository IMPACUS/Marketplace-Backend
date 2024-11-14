package com.impacus.maketplace.dto.alarm.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendUserPushDTO {
    @NotNull(message = "토큰값은 필수입니다.")
    private String token;
}
