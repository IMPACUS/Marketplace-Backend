package com.impacus.maketplace.entity.alarm.user;

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
}
