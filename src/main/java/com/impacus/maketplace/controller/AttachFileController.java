package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.service.CloudFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Path;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/attach-file")
public class AttachFileController {
    private final CloudFileUploadService cloudFileUploadService;

    @PostMapping("")
    public ApiResponseEntity<String> addAttachFile(
            @RequestPart(required = false, value = "image") MultipartFile file) {
        URI result = cloudFileUploadService.uploadFile(file, Path.of(DirectoryConstants.EDITOR_IMAGE_DIRECTORY));

        return ApiResponseEntity
                .<String>builder()
                .data(result.toString())
                .build();
    }

    @DeleteMapping("")
    public ApiResponseEntity<Void> addAttachFile(@RequestParam("file-name") String fileName) {
        URI uri = URI.create(fileName);
        cloudFileUploadService.deleteFile(uri.getPath().substring(1, uri.getPath().length()));

        return ApiResponseEntity
                .<Void>builder()
                .message("업로드 파일 삭제 성공")
                .build();
    }
}
