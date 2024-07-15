package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDTO {
    private Long id;
    private String name;
    private String email;
    private String accountType;
    private String phoneNumber;
    private ZonedDateTime recentActivityDate;
    private String activityDetail;


    @QueryProjection
    public AdminUserDTO(Long id, String name, String email, String accountType, String phoneNumber, ZonedDateTime recentActivityDate, String activityDetail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.recentActivityDate = recentActivityDate;
        this.activityDetail = activityDetail;
    }


}
