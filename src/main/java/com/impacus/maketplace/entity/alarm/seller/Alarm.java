package com.impacus.maketplace.entity.alarm.seller;

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
abstract class Alarm extends BaseEntity {
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

    @Column(columnDefinition = "text")
    @Comment("메세지 내용")
    protected String comment;
}