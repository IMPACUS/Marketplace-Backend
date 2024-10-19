package com.impacus.maketplace.repository.common.querydsl;

import com.impacus.maketplace.dto.common.response.BaseInfoDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseInfoCustomRepository {
    BaseInfoDTO findRewardBaseInfo();

    void updateRewardBaseInfo(BaseInfoDTO dto);
}
