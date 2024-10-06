package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.entity.alarm.seller.enums.AdvertisementEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_seller_advertisement")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmAdvertisement extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_advertisement_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementEnum category;
}
