package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminGroupCountDTO {
    private String accountType;
    private Long count;

    @QueryProjection
    public AdminGroupCountDTO(String accountType, Long count) {
        this.accountType = accountType;
        this.count = count;
    }
}
