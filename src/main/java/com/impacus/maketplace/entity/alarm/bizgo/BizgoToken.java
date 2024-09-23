package com.impacus.maketplace.entity.alarm.bizgo;

import com.impacus.maketplace.common.utils.TimestampConverter;
import com.impacus.maketplace.service.alarm.user.dto.BizgoTokenDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bizgo_token")
@EntityListeners(AuditingEntityListener.class)
public class BizgoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expiredDate;

    @CreatedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime createAt;

    @CreatedBy
    @Column(updatable = false)
    private String registerId;

    public BizgoToken(BizgoTokenDto b) {
        BizgoTokenDto.Data data = b.getData();
        this.token = data.getToken();
        String expiredData = data.getExpired().replace("+09:00", "");
        this.expiredDate = LocalDateTime.parse(expiredData);
    }
}

