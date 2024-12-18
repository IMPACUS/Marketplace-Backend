package com.impacus.maketplace.dto.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import lombok.Getter;

@Getter
public class GetSellerAlarmDTO {
    private AlarmSellerCategoryEnum category;
    private AlarmSellerTimeEnum time;
    private Boolean kakao;
    private Boolean email;
    private Boolean msg;

    public GetSellerAlarmDTO(AlarmSeller a) {
        this.category = a.getCategory();
        this.time = a.getTime();
        this.kakao = a.getKakao();
        this.email = a.getEmail();
        this.msg = a.getMsg();
    }
}
