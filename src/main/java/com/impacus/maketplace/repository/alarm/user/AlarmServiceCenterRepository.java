package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.entity.alarm.user.AlarmOrderDelivery;
import com.impacus.maketplace.entity.alarm.user.AlarmServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmServiceCenterRepository extends JpaRepository<AlarmServiceCenter, Long> {
}
