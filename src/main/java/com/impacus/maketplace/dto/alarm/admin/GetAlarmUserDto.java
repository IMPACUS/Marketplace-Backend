package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Set;

@Data
public class GetAlarmUserDto {
    @NotNull(message = "null 허용 안함")
    private AlarmCategoryUserEnum category;

    @NotNull(message = "null 허용 안함")
    private Set<AlarmSubcategoryUserEnum> subcategory;
}
