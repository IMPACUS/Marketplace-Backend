package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDTO;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    public Slice<GreenLabelHistoryDTO> getGreenLabelPointHistoriesForApp(Long userId, Pageable pageable) {
        try {
            return greenLabelPointHistoryRepository.findHistoriesByUserId(userId, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * [관리자] 포인트 리워드 목록 조회 API
     *
     * @param pageable
     * @param keyword
     * @param status
     * @param startAt
     * @param endAt
     * @return
     */
    public Page<WebGreenLabelHistoryDTO> getGreenLabelPointHistoriesForWeb(
            Pageable pageable,
            String keyword,
            RewardPointStatus status,
            LocalDate startAt,
            LocalDate endAt
    ) {
        try {
            return greenLabelPointHistoryRepository.getGreenLabelPointHistoriesForWeb(pageable, keyword, status, startAt, endAt);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
