package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetAlarmSellerDto {
    private AlarmCategorySellerEnum category;
    private AlarmSubcategorySellerEnum subcategory;
}
