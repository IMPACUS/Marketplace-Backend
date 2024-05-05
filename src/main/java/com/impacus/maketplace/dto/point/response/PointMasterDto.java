package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.entity.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record PointMasterDto(Long id, User userId, Integer userScore, String userLevel, Integer availablePoint) {

    public PointMasterDto(PointMaster pointMaster) {
        this(pointMaster.getId(), pointMaster.getUser(), pointMaster.getUserScore(), pointMaster.getUserLevel().toString(), pointMaster.getAvailablePoint());
    }

}
