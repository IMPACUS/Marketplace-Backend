package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.entity.user.User;
import lombok.Builder;

@Builder
public record PointMasterDTO(Long id, User userId, Integer userScore, String userLevel, Integer availablePoint) {

    public PointMasterDTO(PointMaster pointMaster) {
        this(pointMaster.getId(), pointMaster.getUser(), pointMaster.getUserScore(), pointMaster.getUserLevel().toString(), pointMaster.getAvailablePoint());
    }

}
