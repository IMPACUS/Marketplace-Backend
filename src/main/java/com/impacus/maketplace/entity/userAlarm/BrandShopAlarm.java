package com.impacus.maketplace.entity.userAlarm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brand_shop_alarm")
@AllArgsConstructor
@NoArgsConstructor
public class BrandShopAlarm extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_shop_alarm_id")
    private Long id;
}
