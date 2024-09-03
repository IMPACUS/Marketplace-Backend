package com.impacus.maketplace.dto.alarm.user.get;

import lombok.Getter;

@Getter
public class GetShoppingBenefitsDto extends GetAlarmDto {

    public GetShoppingBenefitsDto(Long id, Boolean kakao, Boolean email, Boolean msg, Boolean push, String comment1, String comment2) {
        super(id, kakao, email, msg, push, comment1, comment2);
    }
}
