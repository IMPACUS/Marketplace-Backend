package com.impacus.maketplace.dto.admin;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserListDto {
    private Long id;
    private Long userId;
    private String email;
    private String password;
//    private String crtDate;
    private String accountType;
//    private String tel;
    private String activityDetail;

    @QueryProjection
    public AdminUserListDto(Long id, Long userId, String email, String password, String accountType, String activityDetail) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.activityDetail = activityDetail;
    }
}
