package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "alarm_seller")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmSeller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmSellerCategoryEnum category;

    private Boolean kakao;
    private Boolean email;
    private Boolean msg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmSellerTimeEnum time;

    public AlarmSeller(Long sellerId, AlarmSellerCategoryEnum category, Boolean kakao, Boolean email, Boolean msg, AlarmSellerTimeEnum time) {
        this.sellerId = sellerId;
        this.category = category;
        this.kakao = kakao;
        this.email = email;
        this.msg = msg;
        this.time = time;
    }

    public void update(Boolean kakao, Boolean email, Boolean msg, AlarmSellerTimeEnum time) {
        this.kakao = kakao;
        this.email = email;
        this.msg = msg;
        this.time = time;
    }
}
