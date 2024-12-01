package com.impacus.maketplace.dto.common.response;

import lombok.Getter;

@Getter
public class BizgoSMSResponse {
    private String code;
    private String result;
    private String msgKey;
    private String ref;

    @Override
    public String toString() {
        return "BizgoSMSResponse [code=" + code + ", result=" + result + "]";
    }
}
