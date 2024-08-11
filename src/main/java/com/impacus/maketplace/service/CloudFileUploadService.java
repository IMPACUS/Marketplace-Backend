package com.impacus.maketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Path;

/**
 * 클라우드 서비스에 파일 업로드 및 삭제 가능한 서비스 인터페이스
 */
public interface CloudFileUploadService {

    /**
     * 클라우드 서비스에 파일 업로드 후 URI 리턴
     *
     * @param file            업로드 할 파일
     * @param parentDirectory 파일의 디렉토리 경로
     * @return 업로드 후 파일의 URI
     */
    URI uploadFile(MultipartFile file, Path parentDirectory);

    /**
     * 클라우드 서비스에서 파일 삭제
     * TODO: 여러 건 동시 삭제 API 필요할 듯
     *
     * @param fileKey 삭제할 파일 키
     */
    boolean deleteFile(String fileKey);

}
