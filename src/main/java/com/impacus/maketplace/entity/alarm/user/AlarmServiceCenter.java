package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
import com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_service_center")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmServiceCenter extends Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_service_center_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceCenterEnum content;
}
