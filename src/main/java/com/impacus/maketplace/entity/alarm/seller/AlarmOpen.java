package com.impacus.maketplace.entity.alarm.seller;

import com.impacus.maketplace.entity.alarm.seller.enums.OpenEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_seller_open")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmOpen extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_open_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenEnum category;
}
