package com.impacus.maketplace.dto.common.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class IdsDTO {
    @NotNull
    private List<Long> ids;
}
