package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.factory.ProductFactory;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.CloudFileUploadService;
import com.impacus.maketplace.service.product.CreateProductService;
import com.impacus.maketplace.service.user.UserDeactivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.net.URI;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/develop")
public class DevelopController {

    private final CloudFileUploadService cloudFileUploadService;
    private final UserDeactivationService userDeactivationService;
    private final CreateProductService createProductService;

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
        userDeactivationService.deleteConsumerByEmail(email);
        return ApiResponseEntity.<UserDTO>builder()
                .message("사용자 삭제 성공")
                .build();
    }

    /**
     * 상품 더미데이터 생성 API
     *
     * @param count 요청할 더미 데이터 수
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("/products/dummy")
    public ApiResponseEntity<Void> addDummyProducts(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(value = "count") int count
    ) {
        for (int i = 0; i < count; i++) {
            CreateProductDTO dto = ProductFactory.createProductDTO(StringUtils.generateRandomString(10));
            createProductService.addProduct(user.getId(), dto);
        }
        return ApiResponseEntity.<Void>builder()
                .message("상품 더미 데이터 생성 성공")
                .build();
    }

}
