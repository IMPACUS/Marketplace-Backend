package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AddAlarmSellerDto {
    @NotNull(message = "null 허용 안함")
    private AlarmCategorySellerEnum category;

    @NotNull(message = "null 허용 안함")
    private Map<AlarmSubcategorySellerEnum, List<String>> subcategory;

    public AlarmAdminForSeller toEntity(AlarmSubcategorySellerEnum subcategoryEnum){
        return new AlarmAdminForSeller(this, subcategoryEnum);
    }
}
