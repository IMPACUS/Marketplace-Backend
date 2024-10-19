package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.CommonGreenLabelPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonGreenLabelPointHistoryRepository extends JpaRepository<CommonGreenLabelPointHistory, Long> {
}
