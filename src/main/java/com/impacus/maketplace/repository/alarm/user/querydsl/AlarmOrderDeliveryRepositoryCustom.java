package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetOrderDeliveryDto;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;

public interface AlarmOrderDeliveryRepositoryCustom {
    GetOrderDeliveryDto findData(OrderDeliveryEnum orderDelivery, Long userId);
}
