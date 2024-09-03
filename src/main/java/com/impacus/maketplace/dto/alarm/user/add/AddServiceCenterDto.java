package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmServiceCenter;
import com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum;
import lombok.Getter;

@Getter
public class AddServiceCenterDto extends AddAlarmDto {
    private ServiceCenterEnum content;

    public AlarmServiceCenter toEntity(Long userId){
        return new AlarmServiceCenter(this, userId);
    }
}
