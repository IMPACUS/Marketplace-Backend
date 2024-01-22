package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.entity.point.PointMaster;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record PointMasterDto(Long id, Long userId, Integer userScore, String userLevel, Integer availablePoint) {

    public PointMasterDto(PointMaster pointMaster) {
        this(pointMaster.getId(), pointMaster.getUserId(), pointMaster.getUserScore(), pointMaster.getUserLevel().toString(), pointMaster.getAvailablePoint());
    }

}
