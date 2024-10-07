package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AddAlarmUserDto {
    @NotNull(message = "null 허용 안함")
    private AlarmCategoryUserEnum category;

    @NotNull(message = "null 허용 안함")
    private Map<AlarmSubcategoryUserEnum, List<String>> subcategory;

    public AlarmAdminForUser toEntity(AlarmSubcategoryUserEnum subcategoryEnum) {
        return new AlarmAdminForUser(this, subcategoryEnum);
    }
}
