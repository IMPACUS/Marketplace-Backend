package com.impacus.maketplace.service.common;

import com.impacus.maketplace.dto.common.response.BaseInfoDTO;
import com.impacus.maketplace.repository.common.BaseInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BaseInfoService {
    private final BaseInfoRepository baseInfoRepository;

    /**
     * 포인트/쿠폰 기준 정보 조회
     *
     * @return
     */
    public BaseInfoDTO findRewardBaseInfo() {
        return baseInfoRepository.findRewardBaseInfo();
    }

    /**
     * 포인트/쿠폰 기준 정보 수정
     *
     * @param dto 수정할 데이터
     */
    @Transactional
    public void updateRewardBaseInfo(BaseInfoDTO dto) {
        baseInfoRepository.updateRewardBaseInfo(dto);
    }
}
