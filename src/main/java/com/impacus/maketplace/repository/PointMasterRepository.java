package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.point.PointMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointMasterRepository extends JpaRepository<PointMaster, Long>, PointMasterCustomRepository {

    boolean existsPointMasterByUserId(Long userId);

    Optional<PointMaster> findByUserId(Long userId);
}
