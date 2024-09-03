package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.dto.alarm.user.add.AddOrderDeliveryDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateBrandShopDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateOrderDeliveryDto;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_order_delivery")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmOrderDelivery extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_order_delivery_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderDeliveryEnum content;

    public AlarmOrderDelivery(AddOrderDeliveryDto s, Long userId) {
        this.userId = userId;
        this.content = s.getContent();
        this.comment1 = s.getComment1();
        this.comment2 = s.getComment2();
        this.email = s.getEmail();
        this.kakao = s.getKakao();
        this.msg = s.getMsg();
        this.push = s.getPush();
    }

    public void updateAlarm(UpdateOrderDeliveryDto u) {
        this.comment1 = u.getComment1();
        this.comment2 = u.getComment2();
        this.email = u.getEmail();
        this.kakao = u.getKakao();
        this.msg = u.getMsg();
        this.push = u.getPush();
    }
}
