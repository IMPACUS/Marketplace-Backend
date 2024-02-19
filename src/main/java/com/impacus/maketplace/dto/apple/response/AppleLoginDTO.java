package com.impacus.maketplace.dto.apple.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppleLoginDTO {
    private String id;
    private Object result;

    public static AppleLoginDTO toDTO(String id, Object result) {
        return new AppleLoginDTO(id, result);
    }
}
