package com.impacus.maketplace.service.point;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import com.impacus.maketplace.repository.point.RewardPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
