package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.dto.alarm.user.add.AddBrandShopDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateBrandShopDto;
import com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_brand_shop")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBrandShop extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_brand_shop_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BrandShopEnum content;

    public AlarmBrandShop(AddBrandShopDto s, Long userId) {
        this.userId = userId;
        this.content = s.getContent();
        this.comment1 = s.getComment1();
        this.comment2 = s.getComment2();
        this.email = s.getEmail();
        this.kakao = s.getKakao();
        this.msg = s.getMsg();
        this.push = s.getPush();
    }

    public void updateAlarm(UpdateBrandShopDto u) {
        this.comment1 = u.getComment1();
        this.comment2 = u.getComment2();
        this.email = u.getEmail();
        this.kakao = u.getKakao();
        this.msg = u.getMsg();
        this.push = u.getPush();
    }
}
