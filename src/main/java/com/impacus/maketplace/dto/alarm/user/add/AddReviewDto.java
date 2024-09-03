package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmReview;
import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
import lombok.Getter;

@Getter
public class AddReviewDto extends AddAlarmDto {
    private ReviewEnum content;

    public AlarmReview toEntity(Long userId){
        return new AlarmReview(this, userId);
    }
}
