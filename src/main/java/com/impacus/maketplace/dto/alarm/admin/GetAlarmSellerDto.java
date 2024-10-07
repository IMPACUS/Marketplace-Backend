package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Set;

@Data
public class GetAlarmSellerDto {
    @NotNull(message = "null 허용 안함")
    private AlarmCategorySellerEnum category;

    @NotNull(message = "null 허용 안함")
    private Set<AlarmSubcategorySellerEnum> subcategory;
}
