package com.impacus.maketplace.dto.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import lombok.Getter;

@Getter
public class GetUserAlarmDto {
    private AlarmUserCategoryEnum category;
    private Boolean isOn;
    private Boolean kakao;
    private Boolean push;
    private Boolean msg;
    private Boolean email;

    public GetUserAlarmDto(AlarmUser a) {
        this.category = a.getCategory();
        this.isOn = a.getIsOn();
        this.kakao = a.getKakao();
        this.push = a.getPush();
        this.msg = a.getMsg();
        this.email = a.getEmail();
    }
}
