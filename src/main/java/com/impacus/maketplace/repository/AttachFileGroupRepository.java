package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.entity.common.AttachFileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachFileGroupRepository extends JpaRepository<AttachFileGroup, Long> {
    List<AttachFileGroup> findByReferencedIdAndReferencedEntity(Long referencedId, ReferencedEntityType entityType);
}
