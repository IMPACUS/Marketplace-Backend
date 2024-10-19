package com.impacus.maketplace.dto.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.impacus.maketplace.common.enumType.common.InfoType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseInfoDetailDTO {

    @NotNull
    private String title;

    @NotNull
    private String detail;

    @JsonIgnore
    private InfoType infoType;
}
