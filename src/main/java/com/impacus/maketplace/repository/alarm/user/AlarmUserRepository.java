package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmUserRepository extends JpaRepository<AlarmUser, Long> {
    Optional<AlarmUser> findByUserIdAndCategory(Long userId, AlarmUserCategoryEnum category);
}
