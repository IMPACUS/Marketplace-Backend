package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetBrandShopDto;
import com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum;

public interface AlarmBrandShopRepositoryCustom {
    GetBrandShopDto findData(BrandShopEnum brandShopEnum, Long userId);
}
