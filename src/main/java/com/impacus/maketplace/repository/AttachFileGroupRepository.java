package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.common.AttachFileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachFileGroupRepository extends JpaRepository<AttachFileGroup, Long> {
}
