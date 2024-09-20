package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Alarm extends BaseEntity {
    @Column(nullable = false)
    protected Long userId;

    @Column(nullable = false)
    @Comment("카카오 알람 여부")
    protected boolean kakao;

    @Column(nullable = false)
    @Comment("이메일 알람 여부")
    protected boolean email;

    @Column(nullable = false)
    @Comment("SNS 알람 여부")
    protected boolean msg;

    @Column(nullable = false)
    @Comment("PUSH 알람 여부")
    protected boolean push;

    @Column
    @Comment("알림 멘트1")
    protected String comment1;

    @Column(columnDefinition = "text")
    @Comment("알림 멘트2")
    protected String comment2;
}
