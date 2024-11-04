package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface GreenLabelPointHistoryCustomRepository {
    Slice<GreenLabelHistoryDTO> findHistoriesByUserId(Long userId, Pageable pageable);

    Page<WebGreenLabelHistoryDTO> getGreenLabelPointHistoriesForWeb(Pageable pageable, String keyword, RewardPointStatus status, LocalDate startAt, LocalDate endAt);

    Page<WebGreenLabelHistoryDetailDTO> getGreenLabelPointHistoryDetailsForWeb(Long userId, Pageable pageable);

    List<WebGreenLabelHistoryDTO> findGreenLabelPointHistoriesByIds(IdsDTO dto);
}
