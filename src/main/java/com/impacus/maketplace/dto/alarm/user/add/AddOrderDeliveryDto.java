package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmOrderDelivery;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;
import lombok.Getter;

@Getter
public class AddOrderDeliveryDto extends AddAlarmDto {
    private OrderDeliveryEnum content;

    public AlarmOrderDelivery toEntity(Long userId){
        return new AlarmOrderDelivery(this, userId);
    }
}
