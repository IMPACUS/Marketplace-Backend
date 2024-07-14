package com.impacus.maketplace.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckExistedEmailDTO {
    @JsonProperty(value = "isExisted")
    private boolean isExisted;

    public static CheckExistedEmailDTO toDTO(boolean isExisted) {
        return new CheckExistedEmailDTO(isExisted);
    }
}
