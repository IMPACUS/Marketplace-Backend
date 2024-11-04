package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.GreenLabelPointHistory;
import com.impacus.maketplace.repository.point.greenLabelPoint.querydsl.GreenLabelPointHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenLabelPointHistoryRepository extends JpaRepository<GreenLabelPointHistory, Long>, GreenLabelPointHistoryCustomRepository {

}
