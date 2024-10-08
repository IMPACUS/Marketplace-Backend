package com.impacus.maketplace.repository.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlarmAdminForSellerRepository extends JpaRepository<AlarmAdminForSeller, Long> {
    Optional<AlarmAdminForSeller> findByCategoryAndSubcategory(AlarmSellerCategoryEnum category, AlarmSellerSubcategoryEnum subcategory);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlarmAdminForSeller a SET a.comment1 = :comment1, a.template = :template WHERE a.id = :id")
    void updateComment(@Param("id") Long id, @Param("comment1") String comment1, @Param("template") String template);
}
