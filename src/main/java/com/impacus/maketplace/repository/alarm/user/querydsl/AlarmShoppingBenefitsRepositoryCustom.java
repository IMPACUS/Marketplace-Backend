package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetShoppingBenefitsDto;
import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsEnum;


public interface AlarmShoppingBenefitsRepositoryCustom {
    GetShoppingBenefitsDto findData(ShoppingBenefitsEnum shoppingBenefitsEnum, Long userId);
}
