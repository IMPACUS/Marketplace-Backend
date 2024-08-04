package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadUserSummaryDTO {
    private Long userId;
    private String name;
    private UserLevel userLevel;
    private String email;
    private Long greenLevelPoint;
    private Long levelPoint;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime registerAt;

    public ReadUserSummaryDTO(
            Long userId,
            String name,
            UserLevel userLevel,
            String email,
            Long greenLevelPoint,
            Long levelPoint,
            String phoneNumber,
            String profileImageUrl,
            LocalDateTime registerAt
    ) {
        this.userId = userId;
        this.name = name;
        this.userLevel = userLevel;
        this.greenLevelPoint = greenLevelPoint;
        this.levelPoint = levelPoint;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.registerAt = registerAt;

        String[] emailParts = email.split("_");
        this.email = emailParts.length > 1 ? emailParts[1] : email;
    }
}
