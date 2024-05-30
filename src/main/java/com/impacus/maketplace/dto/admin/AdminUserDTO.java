package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDTO {
    private Long id;
    private Long userId;
    private String email;
    private String accountType;
    private String phoneNumber;
    private String recentActivityDate;
    private String activityDetail;


    @QueryProjection
    public AdminUserDTO(Long id, Long userId, String email, String accountType, String phoneNumber, String recentActivityDate, String activityDetail) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.recentActivityDate = recentActivityDate;
        this.activityDetail = activityDetail;
    }


}
