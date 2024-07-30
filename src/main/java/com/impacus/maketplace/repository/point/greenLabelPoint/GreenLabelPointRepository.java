package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenLabelPointRepository extends JpaRepository<GreenLabelPoint, Long> {

}
