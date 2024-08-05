package com.impacus.maketplace.repository.point.levelPoint;

import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelPointHistoryRepository extends JpaRepository<LevelPointHistory, Long> {

}
