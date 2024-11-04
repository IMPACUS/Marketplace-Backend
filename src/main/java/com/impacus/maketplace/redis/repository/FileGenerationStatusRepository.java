package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileGenerationStatusRepository extends JpaRepository<FileGenerationStatus, String> {
}
