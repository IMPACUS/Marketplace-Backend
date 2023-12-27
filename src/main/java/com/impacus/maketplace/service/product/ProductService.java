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
    private static final String PRODUCT_IMAGE_DIRECTORY = "productImage";

    /**
     * 새로운 Product를 저장하는 함수
     *
     * @param productRequest
     * @return
     */
    @Transactional
    public ProductDTO addProduct(List<MultipartFile> productImageList, ProductRequest productRequest) {
//        try {
        // 1. productRequest 데이터 유효성 검사
            if (!validateProductRequest(productImageList, productRequest)) {
                throw new CustomException(ErrorType.INVALID_PRODUCT);
            }

        // 3. 상풍 번호 생성
        String productNumber = StringUtils.getProductNumber();

        // 4. Product 저장
        Product newProduct = productRepository.save(new Product(productNumber, productRequest));
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
        productDescriptionService.addProductDescription(productId, productRequest.getDescription());

        // 7. Product option 저장
        productOptionService.addProductOption(productId, productRequest.getProductOptions());

        // 8. Product detail 저장
        productDetailInfoService.addProductDetailInfo(productId, productRequest.getProductDetail());

        return new ProductDTO(newProduct);
//        } catch (Exception ex) {
//            throw new CustomException(ex);
//        }
    }

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     *
     * @param productRequest
     * @return
     */
    public boolean validateProductRequest(List<MultipartFile> productImageList, ProductRequest productRequest) {
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

        // 3. 상품 내부 데이터 확인
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
}
