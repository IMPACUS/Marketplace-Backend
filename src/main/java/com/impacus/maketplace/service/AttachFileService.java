package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.common.AttachFileGroup;
import com.impacus.maketplace.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;
    private final CloudFileUploadService cloudFileUploadService;
    private final AttachFileGroupService attachFileGroupService;

    /**
     * (일대일 관계로) MultipartFile을 S3에 업로드하고, AttachFile 객체로 생성하는 함수
     *
     * @param file
     * @param directoryPath
     * @return
     */
    @Transactional
    public AttachFile uploadFileAndAddAttachFile(MultipartFile file, String directoryPath) {
        String fileName = cloudFileUploadService.uploadFile(file, Path.of(directoryPath)).toString();

        // 1. AttachFile 저장
        AttachFile newAttachFile = AttachFile.builder()
                .attachFileName(fileName)
                .attachFileSize(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .attachFileExt(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .build();
        attachFileRepository.save(newAttachFile);

        return newAttachFile;
    }

    /**
     * (다대다 관계로) MultipartFile을 S3에 업로드하고, AttachFile 객체로 생성하는 함수
     *
     * @param file
     * @param directoryPath
     * @return
     */
    @Transactional
    public AttachFile uploadFileAndAddAttachFile(MultipartFile file, String directoryPath, Long referencedId, ReferencedEntityType entityType) throws IOException {
        String fileName = cloudFileUploadService.uploadFile(file, Path.of(directoryPath)).toString();

        // 1. AttachFile 저장
        AttachFile newAttachFile = AttachFile.builder()
                .attachFileName(fileName)
                .attachFileSize(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .attachFileExt(StringUtils.getFilenameExtension(file.getOriginalFilename()))
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
    public void deleteAttachFileByReferencedId(Long referencedId, ReferencedEntityType referencedEntityType) {
        // 1. AttachFileGroup 찾기
        List<AttachFileGroup> attachFileGroupList = attachFileGroupService.findAttachFileGroupByReferencedIdAndReferencedEntityType(referencedId, referencedEntityType);

        // 2. AttachFileGroup에 연결된 AttachFile 찾기
        List<AttachFile> attachFileList = attachFileGroupList.stream()
                .map(attachFileGroup -> findAttachFileById(attachFileGroup.getAttachFileId()))
                .collect(Collectors.toList());

        // 3. S3에 파일 삭제
        for (AttachFile file : attachFileList) {
            cloudFileUploadService.deleteFile(file.getAttachFileName());
        }

        // 4. AttachFileGroup 삭제
        attachFileGroupService.deleteAllAttachFileGroup(attachFileGroupList);

        // 5. AttachFile 삭제
        attachFileRepository.deleteAllInBatch(attachFileList);
    }

    /**
     * AttachFile 삭제
     *
     * @param attachFileId
     */
    public void deleteAttachFile(Long attachFileId) {
        AttachFile file = findAttachFileById(attachFileId);

        cloudFileUploadService.deleteFile(file.getAttachFileName());
        attachFileRepository.delete(file);
    }

    /**
     * AttachFile 을 id로 찾는 함수
     *
     * @param attachFileId
     * @return
     */
    public AttachFile findAttachFileById(Long attachFileId) {
        return attachFileRepository.findById(attachFileId)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_ATTACH_FILE));
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

    /**
     * AttachFile의 업로드 파일을 변경하는 함수
     *
     * @param attachFileId
     * @param changedFile
     * @param directoryPath
     */
    public void updateAttachFile(Long attachFileId, MultipartFile changedFile, String directoryPath) {
        AttachFile file = findAttachFileById(attachFileId);

        // 1. 업로드된 파일 삭제
        cloudFileUploadService.deleteFile(file.getAttachFileName());

        // 2. s3 업로드
        String fileName = cloudFileUploadService.uploadFile(changedFile, Path.of(directoryPath)).toString();

        // 3. 업데이트
        attachFileRepository.updateFileInfo(
                file.getId(),
                fileName,
                changedFile.getSize(),
                changedFile.getOriginalFilename(),
                StringUtils.getFilenameExtension(changedFile.getOriginalFilename())
        );
    }
}
