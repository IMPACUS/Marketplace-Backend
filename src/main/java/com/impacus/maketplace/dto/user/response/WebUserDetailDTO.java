package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserStatus;
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

    public void updateLoginInfo() {
        this.email = email.split("_")[1];
    }
}
