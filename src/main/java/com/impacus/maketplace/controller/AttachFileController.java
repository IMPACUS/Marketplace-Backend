package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.common.ImagePurpose;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.CloudFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/attach-file")
public class AttachFileController {
    private final AttachFileService attachFileService;
    private final CloudFileUploadService cloudFileUploadService;

    @PostMapping("")
    public ApiResponseEntity<String> addAttachFile(
            @RequestParam(value = "image-purpose") ImagePurpose imagePurpose,
            @RequestPart(required = false, value = "image") MultipartFile file) {
        URI result = attachFileService.uploadImage(imagePurpose, file);

        return ApiResponseEntity
                .<String>builder()
                .data(result.toString())
                .build();
    }

    @DeleteMapping("")
    public ApiResponseEntity<Void> addAttachFile(@RequestParam("file-name") String fileName) {
        cloudFileUploadService.deleteFile(fileName);

        return ApiResponseEntity
                .<Void>builder()
                .message("업로드 파일 삭제 성공")
                .build();
    }
}
