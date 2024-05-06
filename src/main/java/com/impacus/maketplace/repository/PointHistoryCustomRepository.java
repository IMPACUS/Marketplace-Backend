package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDTO;

import java.util.List;

public interface PointHistoryCustomRepository {
    List<PointHistoryDTO> findAllPointHistory(PointHistorySearchDto pointHistorySearchDto);

//    List<Long> findAllNoUseUser(LocalDateTime startDate, LocalDateTime endDate);
}
