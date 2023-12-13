package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseTimeEntity;
import com.impacus.maketplace.common.enumType.MailType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "email_history")
public class EmailHistory extends BaseTimeEntity {

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

    @ColumnDefault("System")
    @Column(name = "receiver_id")
    private String receiverId; // 발신인

    @Column(name = "auth_no")
    private String authNo; // 인증번호

    @Column(name = "send_datetime", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDatetime;

    private transient String registerId ;
    private transient String modifyId;

}