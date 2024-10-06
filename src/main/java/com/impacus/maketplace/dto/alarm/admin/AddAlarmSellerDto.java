package com.impacus.maketplace.dto.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddAlarmSellerDto {
    private AlarmCategorySellerEnum category;
    private AlarmSubcategorySellerEnum subcategory;

    @NotNull(message = "null은 허용 안됩니다")
    private String comment;

    public AlarmAdminForSeller toEntity(){
        return new AlarmAdminForSeller(this);
    }
}
