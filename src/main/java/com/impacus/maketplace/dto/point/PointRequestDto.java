package com.impacus.maketplace.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointRequestDto {
    private String userId;

    private int savePoint;
    @Builder.Default
    private boolean isManual = false;

}
