package com.impacus.maketplace.dto.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import lombok.Getter;

@Getter
public class UpdateUserAlarmDto {
    private AlarmUserCategoryEnum category;
    private Boolean isOn;
    private Boolean kakao;
    private Boolean push;
    private Boolean msg;
    private Boolean email;
}
