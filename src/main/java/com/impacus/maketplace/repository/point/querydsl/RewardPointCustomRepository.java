package com.impacus.maketplace.repository.point.querydsl;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RewardPointCustomRepository {
    Page<RewardPointDTO> getRewardPoints(Pageable pageable, String keyword, RewardPointStatus status);

    long updateRewardPointStatus(List<Long> rewardPointIds, RewardPointStatus status);
}
