package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.user.DormantUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormantUserRepository extends JpaRepository <DormantUser, Long> {
}
