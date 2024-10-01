package com.impacus.maketplace.service.point;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import com.impacus.maketplace.repository.point.RewardPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardPointService {

    private final RewardPointRepository rewardPointRepository;

    public Page<RewardPointDTO> getRewardPoints(Pageable pageable, String keyword, RewardPointStatus status) {
        try {
            return rewardPointRepository.getRewardPoints(pageable, keyword, status);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 포인트 리워드의 발급 상태 변경 함수
     *
     * @param rewardPointIds
     * @param status
     */
    @Transactional
    public void updateRewardPointStatus(List<Long> rewardPointIds, RewardPointStatus status) {
        try {
            long result = rewardPointRepository.updateRewardPointStatus(rewardPointIds, status);
            if (result != rewardPointIds.size()) {
                throw new CustomException(PointErrorType.NOT_EXISTED_REWARD_POINT_ID);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
