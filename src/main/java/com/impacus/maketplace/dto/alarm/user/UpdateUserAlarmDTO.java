package com.impacus.maketplace.dto.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import lombok.Getter;

@Getter
public class UpdateUserAlarmDTO {
    private AlarmUserCategoryEnum category;
    private Boolean isOn;
    private Boolean kakao;
    private Boolean push;
    private Boolean msg;
    private Boolean email;
}
