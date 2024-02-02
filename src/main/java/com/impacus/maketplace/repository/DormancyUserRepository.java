package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.user.DormancyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormancyUserRepository extends JpaRepository<DormancyUser, Long> {
}
