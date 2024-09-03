package com.impacus.maketplace.dto.alarm.user.update;

import lombok.Getter;

@Getter
public abstract class UpdateAlarmDto {
    private Boolean kakao = false;
    private Boolean email = false;
    private Boolean msg = false;
    private Boolean push = false;
    private String comment1 = "";
    private String comment2 = "";
}
