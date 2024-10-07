package com.impacus.maketplace.repository.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface AlarmAdminForUserRepository extends JpaRepository<AlarmAdminForUser, Long> {
    Optional<AlarmAdminForUser> findByCategoryAndSubcategory(AlarmCategoryUserEnum category, AlarmSubcategoryUserEnum subcategory);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlarmAdminForUser a SET a.comment1 = :comment1, a.comment2 = :comment2, a.template = :template WHERE a.id = :id")
    void updateComment(@Param("id") Long id, @Param("comment1") String comment1, @Param("comment2") String comment2, @Param("template") String template);
}
