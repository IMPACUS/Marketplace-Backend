package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.common.enumType.FileStatus;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import com.impacus.maketplace.redis.repository.FileGenerationStatusRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileGenerationStatusService {
    private final FileGenerationStatusRepository fileGenerationStatusRepository;
    private final EntityManager entityManager;

    @Transactional
    public void updateFileGenerationStatus(FileGenerationStatus fileGenerationStatus, FileStatus status) {
        updateFileGenerationStatus(fileGenerationStatus, status, null);
    }

    @Transactional
    public void updateFileGenerationStatus(
            FileGenerationStatus fileGenerationStatus,
            FileStatus status,
            @Nullable String savedURL
    ) {
        fileGenerationStatus.updateStatus(status, savedURL);
        fileGenerationStatusRepository.save(fileGenerationStatus);
        entityManager.flush();
    }
}
