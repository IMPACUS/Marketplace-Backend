package com.impacus.maketplace.repository.user.querydsl;

import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;

public interface ReadUserCustomRepository {
    ReadUserSummaryDTO findUserSummaryByEmail(String email);
}
