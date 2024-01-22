package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryCustomRepository {
    List<PointHistoryDto> findAllPointHistory(PointHistorySearchDto pointHistorySearchDto);

    List<Long> findAllWithNoUseOrSavePoint(LocalDateTime startDate);
}
