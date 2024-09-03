package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.entity.alarm.user.AlarmBrandShop;
import com.impacus.maketplace.repository.alarm.user.querydsl.AlarmBrandShopRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmBrandShopRepository extends JpaRepository<AlarmBrandShop, Long>, AlarmBrandShopRepositoryCustom {
}
