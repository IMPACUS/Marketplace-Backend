package com.impacus.maketplace.dto.admin;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class AdminLoginHistoryDTO {
    private Long userId;
    private String status;

    @QueryProjection
    public AdminLoginHistoryDTO(Long userId, String status) {
        this.userId = userId;
        this.status = status;
    }
}
