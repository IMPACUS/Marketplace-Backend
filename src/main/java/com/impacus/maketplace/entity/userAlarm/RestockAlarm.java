package com.impacus.maketplace.entity.userAlarm;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "restock_alarm")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestockAlarm extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restock_alarm_id")
    private Long id;
}
