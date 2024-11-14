package com.impacus.maketplace.dto.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum;
import lombok.Getter;

@Getter
public class UpdateSellerAlarmDTO {
    private AlarmSellerCategoryEnum category;
    private AlarmSellerTimeEnum time;
    private Boolean kakao;
    private Boolean email;
    private Boolean msg;
}
