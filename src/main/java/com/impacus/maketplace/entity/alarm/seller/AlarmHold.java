package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmHoldEnum;
import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "alarm_hold")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AlarmHold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean kakao;
    private Boolean email;
    private Boolean msg;

    private String subject;
    private String receiver;
    private String phone;
    private String kakaoCode;
    @Column(columnDefinition = "text")
    private String text;

    @CreatedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime createAt;

    @CreatedBy
    @Column(updatable = false)
    private String registerId;

    public AlarmHold(Boolean kakao, Boolean email, Boolean msg, String subject, String receiver, String phone, String kakaoCode, String text) {
        this.kakao = kakao;
        this.email = email;
        this.msg = msg;
        this.subject = subject;
        this.receiver = receiver;
        this.phone = phone;
        this.kakaoCode = kakaoCode;
        this.text = text;
    }
}
