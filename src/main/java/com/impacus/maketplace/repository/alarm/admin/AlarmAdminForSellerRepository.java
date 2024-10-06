package com.impacus.maketplace.repository.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmAdminForSellerRepository extends JpaRepository<AlarmAdminForSeller, Long> {
    Optional<AlarmAdminForSeller> findByCategoryAndSubcategory(AlarmCategorySellerEnum category, AlarmSubcategorySellerEnum subcategory);
}
