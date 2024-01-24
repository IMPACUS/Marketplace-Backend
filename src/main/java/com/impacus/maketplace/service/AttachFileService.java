package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.common.AttachFileGroup;
import com.impacus.maketplace.repository.AttachFileRepository;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 참조하는 객체와 연결되어 있는 AttachFile들을 반환하는 함수
     *
     * @param referencedId
     * @param referencedEntityType
     */
    public List<AttachFile> findAllAttachFile(Long referencedId, ReferencedEntityType referencedEntityType) {
        // 1. AttachFileGroup 찾기
        List<AttachFileGroup> attachFileGroupList = attachFileGroupService.findAttachFileGroupByReferencedIdAndReferencedEntityType(referencedId, referencedEntityType);

        // 2. AttachFileGroup에 연결된 AttachFile 찾기
        return attachFileGroupList.stream()
                .map(attachFileGroup -> findAttachFileById(attachFileGroup.getAttachFileId()))
                .collect(Collectors.toList());
    }

    /**
     * 참조하는 객체와 연결되어 있는 AttachFile 삭제
     *
     * @param referencedId
     * @param referencedEntityType
     */
    public void deleteAttachFile(Long referencedId, ReferencedEntityType referencedEntityType) {
        // 1. AttachFileGroup 찾기
        List<AttachFileGroup> attachFileGroupList = attachFileGroupService.findAttachFileGroupByReferencedIdAndReferencedEntityType(referencedId, referencedEntityType);

        // 2. AttachFileGroup에 연결된 AttachFile 찾기
        List<AttachFile> attachFileList = attachFileGroupList.stream()
                .map(attachFileGroup -> findAttachFileById(attachFileGroup.getAttachFileId()))
                .collect(Collectors.toList());

        // 3. AttachFileGroup 삭제
        attachFileGroupService.deleteAllAttachFileGroup(attachFileGroupList);

        // 4. AttachFile 삭제
        attachFileRepository.deleteAllInBatch(attachFileList);
    }

    /**
     * AttachFile 을 id로 찾는 함수
     *
     * @param attachFileId
     * @return
     */
    public AttachFile findAttachFileById(Long attachFileId) {
        return attachFileRepository.findById(attachFileId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_ATTACH_FILE));
    }

    /**
     * referencedId에 연관된 AttachFile 데이터 반환하는 함수
     *
     * @param referencedId
     * @param referencedEntityType
     * @return
     */
    public List<AttachFileDTO> findAllAttachFileByReferencedId(Long referencedId, ReferencedEntityType referencedEntityType) {
        return attachFileRepository.findAllAttachFileByReferencedId(referencedId, referencedEntityType);
    }
}
