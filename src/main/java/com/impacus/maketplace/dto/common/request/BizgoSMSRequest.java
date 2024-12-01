package com.impacus.maketplace.dto.common.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BizgoSMSRequest {
    private String from;
    private String to;
    private String text;

    public static BizgoSMSRequest toRequest(
            String from, String to, String text
    ) {
        return new BizgoSMSRequest(from, to, text);
    }
}
