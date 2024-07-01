package com.impacus.maketplace.entity.userAlarm;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Alarm extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Comment("전체 알람 설정 여부")
    private boolean isAlarm;

    @Column(nullable = false)
    @Comment("카카오 알람 여부")
    private boolean kakao;

    @Column(nullable = false)
    @Comment("이메일 알람 여부")
    private boolean email;

    @Column(nullable = false)
    @Comment("SNS 알람 여부")
    private boolean sns;

    @Comment("알람 시간 여부")
    private LocalDateTime time;
}
