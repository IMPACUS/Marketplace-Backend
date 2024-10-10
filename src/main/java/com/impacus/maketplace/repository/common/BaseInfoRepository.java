package com.impacus.maketplace.repository.common;

import com.impacus.maketplace.common.enumType.common.InfoType;
import com.impacus.maketplace.entity.common.BaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseInfoRepository extends JpaRepository<BaseInfo, Long> {

    @Query("select bi.infoType FROM BaseInfo bi")
    List<InfoType> findInfoType();
}
