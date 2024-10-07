package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Set;

@Data
public class GetAlarmUserDto {
    @NotNull(message = "null 허용 안함")
    private AlarmUserCategoryEnum category;

    @NotNull(message = "null 허용 안함")
    private Set<AlarmUserSubcategoryEnum> subcategory;
}
