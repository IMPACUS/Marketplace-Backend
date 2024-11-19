package com.impacus.maketplace.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String mobileCo;

    @JsonProperty("CI")
    private String ci;

    @JsonProperty("UTF8_NAME")
    private String utf8Name;

    @JsonProperty("GENDER")
    private String gender;

    @JsonProperty("RES_SEQ")
    private String resSeq;

    @JsonProperty("BIRTHDATE")
    private String birthdate;

    @JsonProperty("NATIONALINFO")
    private String nationalInfo;

    @JsonProperty("AUTH_TYPE")
    private String authType;

    @JsonProperty("NAME")
    private String name;
}
