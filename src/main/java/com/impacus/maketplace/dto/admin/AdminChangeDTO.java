package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminChangeDTO {
    private Long adminId;
    private String accountType;
    private ZonedDateTime recentActivityDate;


    @QueryProjection
    public AdminChangeDTO(Long adminId, String accountType, ZonedDateTime recentActivityDate) {
        this.adminId = adminId;
        this.accountType = accountType;
        this.recentActivityDate = recentActivityDate;
    }
}
