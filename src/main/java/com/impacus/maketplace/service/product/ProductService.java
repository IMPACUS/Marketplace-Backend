package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDescription;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.repository.ProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;
    private final BrandService brandService;
    private final AttachFileService attachFileService;
    private final ProductDescriptionService productDescriptionService;

    private static final int PRODUCT_IMAGE_SIZE_LIMIT = 341172; // (1080 * 1053 * 3 = 3.41172MB 341172byte)
    private static final int PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT = 341172; // (1000 * 8000 * 3 = 24MB)
    private static final String PRODUCT_IMAGE_DIRECTORY = "productImage";
    private static final String PRODUCT_DESCRIPTION_IMAGE_DIRECTORY = "productDescriptionImage";

    /**
     * 새로운 Product를 저장하는 함수
     *
     * @param productRequest
     * @return
     */
    @Transactional
    public ProductDTO addProduct(List<MultipartFile> productImageList, ProductRequest productRequest, List<MultipartFile> productDescriptionImageList) {
        try {
            // 1. productRequest 데이터 유효성 검사
            if (!validateProductRequest(productImageList, productRequest, productDescriptionImageList)) {
                throw new CustomException(ErrorType.INVALID_PRODUCT);
            }

            // 3. 상풍 번호 생성
            String productNumber = StringUtils.getProductNumber();

            // 4. Product 저장
            Product newProduct = productRepository.save(productRequest.toEntity(productNumber));
            Long productId = newProduct.getId();

            // 5. 대표 이미지 저장 및 AttachFileGroup에 연관 관계 매핑 객체 생성
            productImageList.stream()
                    .map(productImage -> {
                        try {
                            return attachFileService.uploadFileAndAddAttachFile(productImage, PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
                        } catch (IOException e) {
                            throw new CustomException(ErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    }).collect(Collectors.toList());

            // 6. Product description 저장
            ProductDescription productDescription = productDescriptionService.addProductDescription(productId, productRequest);

            // 7. 상품 설명 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            productDescriptionImageList.stream()
                    .map(productDescriptionImage -> {
                        try {
                            return attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                        } catch (IOException e) {
                            throw new CustomException(ErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    }).collect(Collectors.toList());

            //8. Product option 저장
            productOptionService.addProductOption(productId, productRequest.getProductOptions());

            // 9. Product detail 저장
            productDetailInfoService.addProductDetailInfo(productId, productRequest.getProductDetail());

            return new ProductDTO(newProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     *
     * @param productRequest
     * @return
     */
    public boolean validateProductRequest(List<MultipartFile> productImageList, ProductRequest productRequest, List<MultipartFile> productDescriptionImageList) {
        Long brandId = productRequest.getBrandId();
        String productName = productRequest.getName();
        DeliveryType deliveryType = productRequest.getDeliveryType();
        SubCategory subCategory = productRequest.getCategoryType();

        // 1. brand 가 존재하는지 확인
        brandService.findBrandById(brandId);

        // 2. 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (productImageList.size() > 5) {
            throw new CustomException(ErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        for (MultipartFile productImage : productImageList) {
            if (productImage.getSize() > PRODUCT_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 3. 상품 설명 이미지 크기 확인
        for (MultipartFile productImage : productDescriptionImageList) {
            if (productImage.getSize() > PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 4. 상품 내부 데이터 확인
        if (productName.length() > 50) {
            throw new CustomException(ErrorType.INVALID_PRODUCT, "상품명은 50자 이내로 가능합니다.");
        } else if (deliveryType == DeliveryType.NONE) {
            throw new CustomException(ErrorType.INVALID_PRODUCT, "알 수 없는 배송타입 입니다.");
        } else if (subCategory == SubCategory.NONE) {
            throw new CustomException(ErrorType.INVALID_PRODUCT, "알 수 없는 카테고리 입니다.");
        } else {
            return true;
        }
    }

    /**
     * productId로 Product를 찾는 함수
     *
     * @param productId
     * @return
     */
    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_PRODUCT));
    }

    /**
     * Product 삭제하는 함수 (isDelete가 true로 변경)
     * 1. ProductOption 삭제
     * 2. ProductDescription 이미지 삭제
     * 3. ProductDescription 삭제
     * 4. Product 대표 이미지 삭제
     * 5. Product 삭제
     *
     * @param productId
     */
    @Transactional
    public void deleteProduct(Long productId) {
        try {
            // 1. Product 존재 확인
            Product deleteProduct = findProductById(productId);

            // 2. ProductOption 삭제
            productOptionService.deleteAllProductionOptionByProductId(deleteProduct.getId());

            // 3. ProductDescription 이미지 삭제
            ProductDescription productDescription = productDescriptionService.findProductDescriptionByProductId(productId);
            attachFileService.deleteAttachFile(productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);

            // 4. ProductDescription 삭제
            productDescriptionService.deleteProductDescription(productDescription);

            // 5. Product의 대표 이미지 삭제
            attachFileService.deleteAttachFile(deleteProduct.getId(), ReferencedEntityType.PRODUCT);

            // 2. 삭제
            productRepository.deleteById(productId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 등록된 상품 정보를 수정하는 API
     *
     * @param productId
     * @param productImageList
     * @param productRequest
     * @param productDescriptionImageList
     * @return
     */
    @Transactional
    public ProductDTO updateProduct(Long productId, List<MultipartFile> productImageList, ProductRequest productRequest, List<MultipartFile> productDescriptionImageList) {
        // 1. Product 찾기
        Product product = findProductById(productId);

        // 2. productRequest 데이터 유효성 검사
        if (!validateProductRequest(productImageList, productRequest, productDescriptionImageList)) {
            throw new CustomException(ErrorType.INVALID_PRODUCT);
        }

        // 3. Product 수정
        product.setProduct(productRequest);
        productRepository.save(product);

        // 4. 대표 이미지 저장 및 AttachFileGroup에 연관 관계 매핑 객체 생성
        attachFileService.deleteAttachFile(product.getId(), ReferencedEntityType.PRODUCT);
        productImageList.stream()
                .map(productImage -> {
                    try {
                        return attachFileService.uploadFileAndAddAttachFile(productImage, PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
                    } catch (IOException e) {
                        throw new CustomException(ErrorType.FAIL_TO_UPLOAD_FILE);
                    }
                }).collect(Collectors.toList());

        // 5. Product description 수정
        ProductDescription productDescription = productDescriptionService.findProductDescriptionByProductId(product.getId());
        productDescription.setDescription(productRequest.getDescription());

        // 6. 상품 설명 이미지 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
        attachFileService.deleteAttachFile(productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
        productDescriptionImageList.stream()
                .map(productDescriptionImage -> {
                    try {
                        return attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                    } catch (IOException e) {
                        throw new CustomException(ErrorType.FAIL_TO_UPLOAD_FILE);
                    }
                }).collect(Collectors.toList());

        //8. Product option 수정
        productOptionService.deleteAllProductionOptionByProductId(product.getId());
        productOptionService.addProductOption(productId, productRequest.getProductOptions());

        // 9. Product detail 수정
        ProductDetailInfo productDetailInfo = productDetailInfoService.findProductDetailInfoByProductId(product.getId());
        productDetailInfo.setProductDetailInfo(productRequest.getProductDetail());

        return new ProductDTO(product);
    }
}
