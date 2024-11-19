package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.CloudFileUploadService;
import com.impacus.maketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@Profile("dev")
@RequestMapping("api/v1/develop")
public class DevelopController {

    private final CloudFileUploadService cloudFileUploadService;
    private final UserService userService;

    @PostMapping("/file")
    public URI uploadOneFile(@RequestPart MultipartFile multipartFile) {
        return cloudFileUploadService.uploadFile(multipartFile, Path.of("test"));
    }

    @DeleteMapping("/file")
    public void deleteOneFile(@RequestParam String fileName) {
        cloudFileUploadService.deleteFile(fileName);
    }

    @DeleteMapping("user")
    public ApiResponseEntity<UserDTO> addUser(@RequestParam(value = "email") String email) {
        userService.deleteConsumer(email);
        return ApiResponseEntity.<UserDTO>builder()
                .message("사용자 삭제 성공")
                .build();
    }
}
