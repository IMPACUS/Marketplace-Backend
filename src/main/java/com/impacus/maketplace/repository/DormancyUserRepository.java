package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.user.DormancyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DormancyUserRepository extends JpaRepository<DormancyUser, Long> {

    List<DormancyUser> findByUpdateDormancyAt(LocalDateTime localDateTime);
}
