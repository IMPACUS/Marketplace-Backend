package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GreenLabelPointHistoryCustomRepository {
    Slice<GreenLabelHistoryDTO> findHistoriesByUserId(Long userId, Pageable pageable);
}
