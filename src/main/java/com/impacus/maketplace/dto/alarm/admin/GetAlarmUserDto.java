package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import lombok.Data;


import java.util.Set;

@Data
public class GetAlarmUserDto {
    private AlarmCategoryUserEnum category;
    private Set<AlarmSubcategoryUserEnum> subcategory;
}
