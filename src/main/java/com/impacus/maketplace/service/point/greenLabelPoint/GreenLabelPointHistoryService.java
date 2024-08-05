package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointHistoryService {
    private final GreenLabelPointHistoryRepository greenLabelPointHistoryRepository;

    /**
     * 리워드 목록 조회하는 API
     *
     * @param userId
     * @param pageable
     * @return
     */
    public Slice<GreenLabelHistoryDTO> getGreenLabelPointHistories(Long userId, Pageable pageable) {
        try {
            return null;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
