package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.response.CurrentPointInfoDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;


public interface PointMasterCustomRepository {

//    PointMasterDto findByUserIdForMyInfo(Long userId);

    CurrentPointInfoDto findByUserIdForMyCurrentPointStatus(Long userId);
}
