package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.entity.admin.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminInfoRepository extends JpaRepository<AdminInfo, Long>, AdminCustomRepository {
    Optional<AdminInfo> findByAdminIdName(String adminIdName);
}
