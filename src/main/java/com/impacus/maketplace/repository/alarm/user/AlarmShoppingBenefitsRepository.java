package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.entity.alarm.user.AlarmOrderDelivery;
import com.impacus.maketplace.entity.alarm.user.AlarmShoppingBenefits;
import com.impacus.maketplace.repository.alarm.user.querydsl.AlarmShoppingBenefitsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmShoppingBenefitsRepository extends JpaRepository<AlarmShoppingBenefits, Long>, AlarmShoppingBenefitsRepositoryCustom {
}
