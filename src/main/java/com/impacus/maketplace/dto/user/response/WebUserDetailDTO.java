package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebUserDetailDTO {
    private Long userId;            // 소비자 아이디
    private String profileImageUrl;  // 프로필 이미지
    private String email;            // 아이디
    private String password;         // 비밀번호 (간편 로그인 사용자는 null)
    private String name;             // 이름
    private String phoneNumber;      // 휴대폰 번호
    private LocalDateTime registerAt;    // 가입일
    private UserLevel userLevel;        // 등급
    private long levelPoint;      // 레벨 포인트
    private long greenLabelPoint; // 가용 포인트
    private UserStatus userStatus;       // 소비자 상태

    @QueryProjection
    public WebUserDetailDTO(
            Long userId,
            String profileImageUr,
            String email,
            String password,
            String name,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            LocalDateTime registerAt,
            UserLevel userLevel,
            long levelPoint,
            long greenLabelPoint,
            UserStatus userStatus
    ) {
        this.userId = userId;
        this.profileImageUrl = profileImageUr;
        this.email = email.split("_")[1];
        this.password = password;
        this.name = name;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.registerAt = registerAt;
        this.userLevel = userLevel;
        this.levelPoint = levelPoint;
        this.greenLabelPoint = greenLabelPoint;
        this.userStatus = userStatus;
    }
}
