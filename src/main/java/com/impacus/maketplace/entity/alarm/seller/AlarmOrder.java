package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.entity.alarm.seller.enums.OrderEnum;
import com.impacus.maketplace.entity.alarm.seller.enums.OrderSubEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_seller_order")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmOrder extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_order_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderEnum category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderSubEnum subcategory;
}
