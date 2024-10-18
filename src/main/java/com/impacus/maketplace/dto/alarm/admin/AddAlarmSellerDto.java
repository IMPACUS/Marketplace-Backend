package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AddAlarmSellerDto {
    @NotNull(message = "null 허용 안함")
    private AlarmSellerCategoryEnum category;

    @NotNull(message = "null 허용 안함")
    private Map<AlarmSellerSubcategoryEnum, List<String>> subcategory;

    public AlarmAdminForSeller toEntity(AlarmSellerSubcategoryEnum subcategoryEnum){
        return new AlarmAdminForSeller(this, subcategoryEnum);
    }
}
