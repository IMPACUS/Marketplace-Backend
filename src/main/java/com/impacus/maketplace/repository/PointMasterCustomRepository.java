package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.response.CurrentPointInfoDTO;


public interface PointMasterCustomRepository {

//    PointMasterDTO findByUserIdForMyInfo(Long userId);

    CurrentPointInfoDTO findByUserIdForMyCurrentPointStatus(Long userId);
}
