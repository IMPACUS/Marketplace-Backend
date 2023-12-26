package com.impacus.maketplace.controller;

import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/seller/new")
    public ResponseEntity<Object> addProduct(
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.addProduct(productImageList, productRequest);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    // TODO 상품 설명에 사용하는 이미지를 저장하는 함수

    // TODO 상품 설명에 사용하는 이미지를 삭제하는 함수
}
