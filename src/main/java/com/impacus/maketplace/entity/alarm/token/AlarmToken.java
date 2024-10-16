package com.impacus.maketplace.entity.alarm.token;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmTokenEnum;
import com.impacus.maketplace.dto.alarm.bizgo.BizgoTokenDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "alarm_token")
public class AlarmToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmTokenEnum type;

    private String token;
    private LocalDateTime expiredDate;

    public AlarmToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
        this.type = AlarmTokenEnum.FCM;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AlarmToken(BizgoTokenDto b) {
        BizgoTokenDto.Data data = b.getData();
        this.type = AlarmTokenEnum.BIZGO;
        this.token = data.getToken();
        String expiredData = data.getExpired().replace("+09:00", "");
        this.expiredDate = LocalDateTime.parse(expiredData);
    }
}

