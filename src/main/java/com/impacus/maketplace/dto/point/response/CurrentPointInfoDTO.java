package com.impacus.maketplace.dto.point.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record CurrentPointInfoDTO(Integer holdingPoint, Integer scheduledToDisappearPoint) {

    @QueryProjection
    public CurrentPointInfoDTO(Integer holdingPoint, Integer scheduledToDisappearPoint) {
        this.holdingPoint = holdingPoint;
        this.scheduledToDisappearPoint = scheduledToDisappearPoint;
    }
}
