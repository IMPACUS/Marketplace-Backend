package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminInfoDTO {
    private Long id;
    private Long userId;
    private String accountType;
    private String activityDetail;


    @QueryProjection
    public AdminInfoDTO(Long id, Long userId, String accountType, String activityDetail) {
        this.id = id;
        this.userId = userId;
        this.accountType = accountType;
        this.activityDetail = activityDetail;
    }
}
