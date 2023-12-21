package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "email_history")
public class EmailHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MailType mailType; // 메일 타입

    @Column(name = "receive_email", nullable = false)
    private String receiveEmail; // 수신인

    @ColumnDefault("'implace'")
    @Column(name = "receiver_id",nullable = false)
    private String receiverId; // 발신인

    @Column(name = "auth_no")
    private String authNo; // 인증번호

    @CreatedDate
    @Column(name = "send_at", nullable = false)
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime sendAt;

    private transient String registerId ;
    private transient String modifyId;

}