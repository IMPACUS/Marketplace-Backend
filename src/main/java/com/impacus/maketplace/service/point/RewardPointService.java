package com.impacus.maketplace.service.point;

import com.impacus.maketplace.repository.point.RewardPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardPointService {

    private final RewardPointRepository rewardPointRepository;
}
