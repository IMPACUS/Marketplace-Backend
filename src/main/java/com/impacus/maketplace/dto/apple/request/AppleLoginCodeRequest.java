package com.impacus.maketplace.dto.apple.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppleLoginCodeRequest {
    private String code;
    private String id_token;
    private String state;
    private Object user;
}
