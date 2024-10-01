package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface GreenLabelPointHistoryCustomRepository {
    Slice<GreenLabelHistoryDTO> findHistoriesByUserId(Long userId, Pageable pageable);

    Page<WebGreenLabelHistoryDTO> getGreenLabelPointHistoriesForWeb(Pageable pageable, String keyword, RewardPointStatus status, LocalDate startAt, LocalDate endAt);
}
