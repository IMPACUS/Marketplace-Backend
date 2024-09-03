package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetServiceCenterDto;
import com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum;

public interface AlarmServiceCenterRepositoryCustom {
    GetServiceCenterDto findData(ServiceCenterEnum serviceCenterEnum, Long userId);
}
