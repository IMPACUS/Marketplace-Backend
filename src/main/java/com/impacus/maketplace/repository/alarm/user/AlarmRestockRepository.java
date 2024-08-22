package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.entity.alarm.user.AlarmOrderDelivery;
import com.impacus.maketplace.entity.alarm.user.AlarmRestock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRestockRepository extends JpaRepository<AlarmRestock, Long> {
}
