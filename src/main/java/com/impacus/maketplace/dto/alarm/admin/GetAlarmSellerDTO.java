package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Set;

@Data
public class GetAlarmSellerDTO {
    @NotNull(message = "null 허용 안함")
    private AlarmSellerCategoryEnum category;

    @NotNull(message = "null 허용 안함")
    private Set<AlarmSellerSubcategoryEnum> subcategory;
}
