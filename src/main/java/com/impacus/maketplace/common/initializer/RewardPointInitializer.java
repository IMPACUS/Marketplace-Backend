package com.impacus.maketplace.common.initializer;

import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.entity.point.RewardPoint;
import com.impacus.maketplace.repository.point.RewardPointRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class RewardPointInitializer {
    private final RewardPointRepository rewardPointRepository;

    public RewardPointInitializer(RewardPointRepository rewardPointRepository) {
        this.rewardPointRepository = rewardPointRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyRewardPointData() {
        try {
            List<RewardPointType> types = new ArrayList<>(Arrays.stream(RewardPointType.values()).toList());
            rewardPointRepository.deleteByNotInRewardPointType(types);

            Set<RewardPointType> savedTypes = new HashSet<>(rewardPointRepository.findRewardPointType());

            // 1. 생성할 데이터가 존재하는지 확인
            types.removeAll(savedTypes);
            if (types.isEmpty()) {
                return; // 저장할 타입이 없음
            }

            // 2. RewardPointType 중 저장되지 않은 type 생성
            List<RewardPoint> rewardPoints = types.stream()
                    .map(RewardPoint::from)
                    .toList();

            // 저장
            rewardPointRepository.saveAll(rewardPoints);
        } catch (Exception e) {
            LogUtils.error("generateDummyRewardPointData", "Fail to generate dummy reward point data", e);
        }
    }
}
