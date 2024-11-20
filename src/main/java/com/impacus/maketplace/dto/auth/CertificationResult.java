package com.impacus.maketplace.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.entity.consumer.Consumer;
import lombok.Data;

@Data
public class CertificationResult {
    @JsonProperty("REQ_SEQ")
    private String reqSeq;

    @JsonProperty("MOBILE_NO")
    private String mobileNo;

    @JsonProperty("DI")
    private String di;

    @JsonProperty("MOBILE_CO")
    private String mobileCo; // 통신사 정보

    @JsonProperty("CI")
    private String ci;

    @JsonProperty("UTF8_NAME")
    private String utf8Name;

    @JsonProperty("GENDER")
    private String gender; // 성별

    @JsonProperty("RES_SEQ")
    private String resSeq;

    @JsonProperty("BIRTHDATE")
    private String birthdate; // 생년월일

    @JsonProperty("NATIONALINFO")
    private String nationalInfo; // 내/외국인

    @JsonProperty("AUTH_TYPE")
    private String authType; // 인증수단

    @JsonProperty("NAME")
    private String name;

    public Consumer toEntity(Long userId) {
        return new Consumer(userId, this.ci);
    }
}
