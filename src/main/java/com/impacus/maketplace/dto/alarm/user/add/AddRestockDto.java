package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmRestock;
import com.impacus.maketplace.entity.alarm.user.enums.RestockEnum;
import lombok.Getter;

@Getter
public class AddRestockDto extends AddAlarmDto {
    private RestockEnum content;

    public AlarmRestock toEntity(Long userId){
        return new AlarmRestock(this, userId);
    }
}
