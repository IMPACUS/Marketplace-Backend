package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OutputAlarmSellerDto {
    private AlarmCategorySellerEnum category;
    private AlarmSubcategorySellerEnum subcategory;
    private List<String> commentList;
}
