package com.impacus.maketplace.controller;

import com.impacus.maketplace.service.aws.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/develop")
public class DevelopController {

    private final S3Service s3Service;

    @PostMapping("upload-file")
    public ResponseEntity<Object> uploadOneFile(@RequestPart MultipartFile multipartFile)
            throws IOException {
        String uploadedFileURL = s3Service.uploadFileInS3(multipartFile, "test");
        return new ResponseEntity<>(uploadedFileURL, HttpStatus.OK);
    }

    @DeleteMapping("delete-file")
    public ResponseEntity<Object> deleteOneFile(@RequestParam String fileName) {
        s3Service.deleteFileInS3(fileName);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
