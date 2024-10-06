package com.impacus.maketplace.dto.alarm.bizgo;

import com.impacus.maketplace.entity.alarm.bizgo.BizgoToken;
import lombok.Getter;

@Getter
public class BizgoTokenDto {
    private String code;
    private String result;
    private Data data;

    @Getter
    public static class Data {
        private String token;
        private String schema;
        private String expired;
    }

    public BizgoToken toEntity() {
        return new BizgoToken(this);
    }
}
