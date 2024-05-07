package com.impacus.maketplace.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminLoginActivityDTO {
    private Long id;
    private Long userId;
    private ZonedDateTime crtDate;
    private String activityDetail;
}
