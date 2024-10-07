package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "alarm_user")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmUserCategoryEnum category;

    private Boolean isOn;
    private Boolean kakao;
    private Boolean push;
    private Boolean msg;
    private Boolean email;

    public AlarmUser(Long userId, AlarmUserCategoryEnum category, Boolean kakao, Boolean push, Boolean msg, Boolean email) {
        this.userId = userId;
        this.category = category;
        this.isOn = true;
        this.kakao = kakao;
        this.push = push;
        this.msg = msg;
        this.email = email;
    }

    public void update(Boolean isOn, Boolean kakao, Boolean push, Boolean msg, Boolean email) {
        this.isOn = isOn;
        this.kakao = kakao;
        this.push = push;
        this.msg = msg;
        this.email = email;
    }
}
