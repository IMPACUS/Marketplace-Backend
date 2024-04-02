package com.impacus.maketplace.dto.point.request;

import com.impacus.maketplace.common.enumType.point.PointManageType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointManageDto {

    @NotNull
    private Long userId; // 지급, 수취하고자 하는 유저의 아이디
    @NotNull
    private PointManageType pointManageType; // [지급 , 수취]
    private int manageAvailablePoint;        // 관리할 가용 포인트
    private int manageLevelPoint;            // 관리할 레벨 포인트

}
