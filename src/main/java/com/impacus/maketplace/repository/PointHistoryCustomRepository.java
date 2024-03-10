package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;

import java.util.List;

public interface PointHistoryCustomRepository {
    List<PointHistoryDto> findAllPointHistory(PointHistorySearchDto pointHistorySearchDto);

//    List<Long> findAllNoUseUser(LocalDateTime startDate, LocalDateTime endDate);
}
