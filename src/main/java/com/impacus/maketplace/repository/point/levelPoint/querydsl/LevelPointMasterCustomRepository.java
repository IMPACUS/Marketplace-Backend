package com.impacus.maketplace.repository.point.levelPoint.querydsl;

import com.impacus.maketplace.dto.point.levelPoint.LevelPointDTO;

public interface LevelPointMasterCustomRepository {
    LevelPointDTO findPointInformationByUserId(Long userId);
}
