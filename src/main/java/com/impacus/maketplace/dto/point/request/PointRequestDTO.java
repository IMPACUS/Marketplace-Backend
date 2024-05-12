package com.impacus.maketplace.dto.point.request;

import com.impacus.maketplace.common.enumType.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointRequestDTO {
    private Long userId;

    private int savePoint;

    @Builder.Default
    private boolean isManual = false;

    private String pointCode;

    public PointType getPointTypeEnum() {
        return PointType.fromCode(this.pointCode);
    }
}
