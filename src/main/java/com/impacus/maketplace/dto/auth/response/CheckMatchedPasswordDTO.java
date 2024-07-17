package com.impacus.maketplace.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckMatchedPasswordDTO {
    @JsonProperty(value = "isPasswordMatch")
    private boolean isPasswordMatch;

    public static CheckMatchedPasswordDTO toDTO(boolean isPasswordMatch) {
        return new CheckMatchedPasswordDTO(isPasswordMatch);
    }
}
