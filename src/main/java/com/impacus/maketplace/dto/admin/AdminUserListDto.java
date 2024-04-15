package com.impacus.maketplace.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserListDto {
    private Long id;
    private Long userId;
    private String email;
    private String crtDate;
    private String tel;
    private String activityDetail;
}
