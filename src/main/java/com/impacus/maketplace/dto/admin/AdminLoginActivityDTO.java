package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminLoginActivityDTO {
    private Long userId;
    private ZonedDateTime crtDate;
    private String activityDetail;


    @QueryProjection
    public AdminLoginActivityDTO(Long userId, ZonedDateTime crtDate, String activityDetail) {
        this.userId = userId;
        this.crtDate = crtDate;
        this.activityDetail = activityDetail;
    }

    @QueryProjection
    public AdminLoginActivityDTO(ZonedDateTime crtDate, String activityDetail) {
        this.crtDate = crtDate;
        this.activityDetail = activityDetail;
    }


}
