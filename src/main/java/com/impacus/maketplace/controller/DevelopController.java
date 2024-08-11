package com.impacus.maketplace.controller;

import com.impacus.maketplace.service.CloudFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/develop")
public class DevelopController {

    private final CloudFileUploadService cloudFileUploadService;

    @PostMapping("/file")
    public URI uploadOneFile(@RequestPart MultipartFile multipartFile) {
        return cloudFileUploadService.uploadFile(multipartFile, Path.of("test"));
    }

    @DeleteMapping("/file")
    public void deleteOneFile(@RequestParam String fileName) {
        cloudFileUploadService.deleteFile(fileName);
    }

}
