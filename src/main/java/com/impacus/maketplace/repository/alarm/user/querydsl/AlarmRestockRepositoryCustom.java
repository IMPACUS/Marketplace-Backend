package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetRestockDto;
import com.impacus.maketplace.entity.alarm.user.enums.RestockEnum;

public interface AlarmRestockRepositoryCustom {
    GetRestockDto findData(RestockEnum restockEnum, Long userId);
}
