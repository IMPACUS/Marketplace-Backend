package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.common.AttachFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFile, Long>, AttachFileCustomRepository {
    @Transactional
    @Modifying
    @Query("UPDATE AttachFile af " +
            "SET af.attachFileName = :fileName, af.attachFileSize = :fileSize, af.originalFileName = :originalFileName, af.attachFileExt = :attachFileExt " +
            "WHERE af.id = :attachFileId")
    int updateFileInfo(
            @Param("attachFileId") Long attachFileId,
            @Param("fileName") String fileName,
            @Param("fileSize") Long fileSize,
            @Param("originalFileName") String originalFileName,
            @Param("attachFileExt") String attachFileExt);
}
