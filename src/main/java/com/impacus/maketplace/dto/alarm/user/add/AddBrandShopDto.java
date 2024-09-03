package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmBrandShop;
import com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum;
import lombok.Getter;

@Getter
public class AddBrandShopDto extends AddAlarmDto {
    private BrandShopEnum content;

    public AlarmBrandShop toEntity(Long userId){
        return new AlarmBrandShop(this, userId);
    }
}
