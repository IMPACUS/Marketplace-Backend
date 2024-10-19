package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.OrderGreenLabelPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderGreenLabelPointHistoryRepository extends JpaRepository<OrderGreenLabelPointHistory, Long> {
}
