package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OutputAlarmUserDto {
    private AlarmCategoryUserEnum category;
    private AlarmSubcategoryUserEnum subcategory;
    private List<String> commentList;
}
