package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.user.DormantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DormantUserRepository extends JpaRepository <DormantUser, Long> {

    List<DormantUser> findByDormancyUpdateDateTime(LocalDateTime todayDateTime);
}
