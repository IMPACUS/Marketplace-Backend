package com.impacus.maketplace.repository.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmSellerRepository extends JpaRepository<AlarmSeller, Long> {
    Optional<AlarmSeller> findByUserIdAndCategory(Long userId, AlarmSellerCategoryEnum category);

    List<AlarmSeller> findAllByUserId(Long userId);
}
