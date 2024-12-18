package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AddAlarmUserDTO {
    @NotNull(message = "null 허용 안함")
    private AlarmUserCategoryEnum category;

    @NotNull(message = "null 허용 안함")
    private Map<AlarmUserSubcategoryEnum, List<String>> subcategory;

    public AlarmAdminForUser toEntity(AlarmUserSubcategoryEnum subcategoryEnum) {
        return new AlarmAdminForUser(this, subcategoryEnum);
    }
}
