package com.impacus.maketplace.dto.alarm.bizgo;

import com.impacus.maketplace.entity.alarm.token.AlarmToken;
import lombok.Getter;

@Getter
public class BizgoTokenDTO {
    private String code;
    private String result;
    private Data data;

    @Getter
    public static class Data {
        private String token;
        private String schema;
        private String expired;
    }

    public AlarmToken toEntity() {
        return new AlarmToken(this);
    }
}
