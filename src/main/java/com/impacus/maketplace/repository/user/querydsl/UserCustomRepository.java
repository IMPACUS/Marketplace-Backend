package com.impacus.maketplace.repository.user.querydsl;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;

import java.util.List;

public interface UserCustomRepository {
    ReadUserSummaryDTO findUserSummaryByEmail(String email);

    /**
     * 레벨 등급에 해당하는 사용자 id 리스트 반환
     *
     * @param userLevel
     * @return
     */
    List<Long> findUserIdByUserLevel(UserLevel userLevel);

    void deleteConsumer(Long userId);
}
