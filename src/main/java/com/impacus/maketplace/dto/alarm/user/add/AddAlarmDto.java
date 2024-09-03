package com.impacus.maketplace.dto.alarm.user.add;


import lombok.Getter;

@Getter
public abstract class AddAlarmDto {
    private Boolean kakao = false;
    private Boolean email = false;
    private Boolean msg = false;
    private Boolean push = false;
    private String comment1 = "";
    private String comment2 = "";
}
