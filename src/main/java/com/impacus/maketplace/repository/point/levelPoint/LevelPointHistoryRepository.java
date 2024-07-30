package com.impacus.maketplace.repository.point.levelPoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;

@Repository
public interface LevelPointHistoryRepository extends JpaRepository<LevelPointHistory, Long>{
    
}
