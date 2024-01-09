package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryCustomRepository {

    List<PointHistory> findByPointMasterId(Long pointMasterId);
}
