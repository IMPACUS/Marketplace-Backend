package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminLoginHistoryDTO {
    private Long id;
    private Long userId;
    private ZonedDateTime crtDate;
    private String status;

    @QueryProjection
    public AdminLoginHistoryDTO(Long userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    @QueryProjection
    public AdminLoginHistoryDTO(Long id, ZonedDateTime crtDate, String status) {
        this.id = id;
        this.crtDate = crtDate;
        this.status = status;
    }
}
