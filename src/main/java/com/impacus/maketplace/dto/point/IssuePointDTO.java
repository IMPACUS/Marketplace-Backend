package com.impacus.maketplace.dto.point;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssuePointDTO {

    @ValidEnum(enumClass = UserLevel.class, nullable = true)
    private UserLevel userLevel;

    private String email;

    @Min(1)
    private Long levelPoint;

    @Min(1)
    private Long greenLabelPoint;

    @ValidEnum(enumClass = PointStatus.class)
    private PointStatus pointStatus;
}
