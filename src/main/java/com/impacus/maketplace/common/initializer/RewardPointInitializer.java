package com.impacus.maketplace.common.initializer;

import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.entity.point.RewardPoint;
import com.impacus.maketplace.repository.point.RewardPointRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RewardPointInitializer {
    private final RewardPointRepository rewardPointRepository;

    public RewardPointInitializer(RewardPointRepository rewardPointRepository) {
        this.rewardPointRepository = rewardPointRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyRewardPointData() {
        List<RewardPointType> types = new ArrayList<>(Arrays.stream(RewardPointType.values()).toList());
        List<RewardPointType> savedTypes = rewardPointRepository.findRewardPointType();

        // 1. 생성할 데이터가 존재하는지 확인
        types.removeAll(savedTypes);
        if (types.isEmpty()) {
            return;
        }

        // 2. RewardPointType 중 저장되지 않은 type 생성
        List<RewardPoint> rewardPoints = new ArrayList<>();
        for (RewardPointType type : types) {
            if (!savedTypes.contains(type)) {
                rewardPoints.add(RewardPoint.from(type));
            }
        }

        // 저장
        rewardPointRepository.saveAll(rewardPoints);
    }
}
