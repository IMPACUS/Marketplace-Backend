package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliverySubEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_user_order_delivery")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmOrderDelivery extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_order_delivery_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderDeliveryEnum category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderDeliverySubEnum subcategory;
}
