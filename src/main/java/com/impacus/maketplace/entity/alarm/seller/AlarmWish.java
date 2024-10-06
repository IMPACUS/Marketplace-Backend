package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.entity.alarm.seller.enums.WishEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_seller_wish")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmWish extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_wish_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WishEnum category;
}
