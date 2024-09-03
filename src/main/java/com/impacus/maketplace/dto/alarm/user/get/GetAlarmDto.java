package com.impacus.maketplace.dto.alarm.user.get;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class GetAlarmDto {
    private Long id;
    private Boolean kakao;
    private Boolean email;
    private Boolean msg;
    private Boolean push;
    private String comment1;
    private String comment2;
}
