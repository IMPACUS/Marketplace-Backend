package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebUserDTO {

    private Long userId;              // 소비자 아이디

    @ExcelColumn(headerName = "가입채널")
    private OauthProviderType oauthProviderType;  // 가입 채널
    @ExcelColumn(headerName = "이름")
    private String name;               // 이름
    @ExcelColumn(headerName = "아이디")
    private String email;              // 아이디
    @ExcelColumn(headerName = "휴대폰 번호")
    private String phoneNumber;        // 휴대폰 번호
    @ExcelColumn(headerName = "그린라벨")
    private UserLevel userLevel;          // 그린레벨
    @ExcelColumn(headerName = "가입일")
    private LocalDateTime registerAt;      // 가입일
    @ExcelColumn(headerName = "최근 활동")
    private LocalDateTime recentLoginAt;   // 최근 활동일

    @QueryProjection
    public WebUserDTO(
            Long userId,
            String name,
            String email,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            UserLevel userLevel,
            LocalDateTime registerAt,
            LocalDateTime recentLoginAt,
            OauthProviderType oauthProviderType
    ) {
        this.userId = userId;
        this.name = name;
        this.oauthProviderType = oauthProviderType;
        this.email = email;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.userLevel = userLevel;
        this.registerAt = registerAt;
        this.recentLoginAt = recentLoginAt;
    }
}
