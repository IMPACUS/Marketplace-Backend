package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebUserDTO {
    private Long userId;              // 소비자 아이디
    private String oauthProviderType;  // 가입 채널
    private String name;               // 이름
    private String email;              // 아이디
    private String phoneNumber;        // 휴대폰 번호
    private UserLevel userLevel;          // 그린레벨
    private LocalDateTime registerAt;      // 가입일
    private LocalDateTime recentLoginAt;   // 최근 활동일

    @QueryProjection
    public WebUserDTO(
            Long userId,
            String name,
            String email,
            String phoneNumber,
            UserLevel userLevel,
            LocalDateTime registerAt,
            LocalDateTime recentLoginAt
    ) {
        this.userId = userId;
        this.name = name;
        String[] emailInfo = email.split("_");
        this.oauthProviderType = emailInfo[0];
        this.email = emailInfo[1];
        this.phoneNumber = phoneNumber;
        this.userLevel = userLevel;
        this.registerAt = registerAt;
        this.recentLoginAt = recentLoginAt;
    }
}
