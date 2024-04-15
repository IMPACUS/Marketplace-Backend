package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.entity.admin.AdminLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLoginLogRepository extends JpaRepository<AdminLoginLog, Long>, AdminCustomRepository {
}
