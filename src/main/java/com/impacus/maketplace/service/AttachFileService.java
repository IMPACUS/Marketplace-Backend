package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.repository.AttachFileRepository;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;
    private final S3Service s3Service;
    private final AttachFileGroupService attachFileGroupService;

    /**
     * MultipartFile을 S3에 업로드하고, AttachFile 객체로 생성하는 함수
     *
     * @param file
     * @param directoryPath
     * @return
     */
    @Transactional
    public AttachFile uploadFileAndAddAttachFile(MultipartFile file, String directoryPath, Long referencedId, ReferencedEntityType entityType) throws IOException {
        String fileName = s3Service.uploadFileInS3(file, directoryPath);

        // 1. AttachFile 저장
        AttachFile newAttachFile = AttachFile.builder()
                .attachFileName(fileName)
                .attachFileSize(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .attachFileExt(StringUtils.getFileExtension(file.getOriginalFilename()).orElse(null))
                .build();
        attachFileRepository.save(newAttachFile);

        // 2. AttachFileGroup 저장
        attachFileGroupService.saveAttachFileGroup(newAttachFile.getId(), referencedId, entityType);

        return newAttachFile;
    }
}
