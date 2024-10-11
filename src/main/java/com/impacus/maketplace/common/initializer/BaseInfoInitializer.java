package com.impacus.maketplace.common.initializer;

import com.impacus.maketplace.common.enumType.common.InfoType;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.entity.common.BaseInfo;
import com.impacus.maketplace.repository.common.BaseInfoRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class BaseInfoInitializer {
    private final BaseInfoRepository baseInfoRepository;

    public BaseInfoInitializer(BaseInfoRepository baseInfoRepository) {
        this.baseInfoRepository = baseInfoRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyBaseInfo() {
        try {
            List<InfoType> types = new ArrayList<>(Arrays.stream(InfoType.values()).toList());
            Set<InfoType> savedTypes = new HashSet<>(baseInfoRepository.findInfoType());

            // 1. 생성할 데이터가 존재하는지 확인
            types.removeAll(savedTypes);
            if (types.isEmpty()) {
                return;
            }

            // 2. InfoType 중 저장되지 않은 type 생성
            List<BaseInfo> baseInfo = types.stream()
                    .map(BaseInfo::from)
                    .toList();

            // 저장
            baseInfoRepository.saveAll(baseInfo);
        } catch (Exception e) {
            LogUtils.error("generateDummyBaseInfo", "Fail to generate dummy base info", e);
        }
    }
}
