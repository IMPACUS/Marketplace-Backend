package com.impacus.maketplace.dto.point;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmPointDTO {
    private String userName; // 사용자 이름
    private String phoneNumber; // 사용자 핸드폰 번호
    private Long remainPoint; // 소멸될 남은 포인트
    private LocalDateTime expiredAt; // 포인트 소멸 날짜
}
