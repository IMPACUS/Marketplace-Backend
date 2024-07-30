package com.impacus.maketplace.repository.point.greenLabelPoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;

@Repository
public interface GreenLabelPointAllocationRepository extends JpaRepository<GreenLabelPointAllocation, Long>{
    
}
