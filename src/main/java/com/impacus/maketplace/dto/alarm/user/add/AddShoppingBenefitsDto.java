package com.impacus.maketplace.dto.alarm.user.add;

import com.impacus.maketplace.entity.alarm.user.AlarmShoppingBenefits;
import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsEnum;
import lombok.Getter;

@Getter
public class AddShoppingBenefitsDto extends AddAlarmDto {
    private ShoppingBenefitsEnum content;

    public AlarmShoppingBenefits toEntity(Long userId){
        return new AlarmShoppingBenefits(this, userId);
    }
}
