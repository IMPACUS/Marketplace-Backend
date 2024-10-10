package com.impacus.maketplace.repository.common;

import com.impacus.maketplace.entity.common.BaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseInfoRepository extends JpaRepository<BaseInfo, Long> {
}
