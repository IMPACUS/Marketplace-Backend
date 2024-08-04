package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;
import com.impacus.maketplace.repository.point.greenLabelPoint.querydsl.GreenLabelPointAllocationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenLabelPointAllocationRepository extends JpaRepository<GreenLabelPointAllocation, Long>, GreenLabelPointAllocationCustomRepository {
}
