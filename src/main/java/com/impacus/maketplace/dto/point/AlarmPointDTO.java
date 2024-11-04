package com.impacus.maketplace.dto.point;

import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmPointDTO {
    private Long userId;
    private String userName; // 사용자 이름
    private String phoneNumber; // 사용자 핸드폰 번호
    private String email; // 사용자 이메일 주소
    private Long remainPoint; // 소멸될 남은 포인트
    private LocalDateTime expiredAt; // 포인트 소멸 날짜

    public AlarmPointDTO(
            Long userId,
            String userName,
            Long remainPoint,
            LocalDateTime expiredAt,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            String email
    ) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.email = email;
        this.remainPoint = remainPoint;
        this.expiredAt = expiredAt;
    }
}
