package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetReviewDto;
import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;

public interface AlarmReviewRepositoryCustom {
    GetReviewDto findData(ReviewEnum reviewEnum, Long userId);
}
