package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDescription;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.redis.service.RecentProductViewsService;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.repository.product.WishlistRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.seller.SellerService;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;
    private final SellerService sellerService;
    private final AttachFileService attachFileService;
    private final ProductDescriptionService productDescriptionService;
    private final TemporaryProductService temporaryProductService;
    private final SubCategoryService subCategoryService;
    private final WishlistRepository wishlistRepository;
    private final ProductDeliveryTimeService deliveryTimeService;
    private final RecentProductViewsService recentProductViewsService;
    private final ProductClaimService productClaimService;

    /**
     * 새로운 Product 생성 함수
     *
     * @param dto
     * @return
     */
    @Transactional
    public ProductDTO addProduct(
            Long userId,
            List<MultipartFile> productImageList,
            CreateProductDTO dto,
            List<MultipartFile> productDescriptionImageList) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);

            // 1. productRequest 데이터 유효성 검사
            validateProductRequest(
                    productImageList, dto.getCategoryId(), productDescriptionImageList
            );

            // 2. 상풍 번호 생성
            String productNumber = StringUtils.getProductNumber();

            // 3. Product 저장
            Product newProduct = productRepository.save(dto.toEntity(productNumber, seller.getId()));
            Long productId = newProduct.getId();

            // 4. 대표 이미지 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            productImageList
                    .forEach(productImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productImage, DirectoryConstants.PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            // 5. Product description 저장
            ProductDescription productDescription = productDescriptionService.addProductDescription(productId, dto);

            // 6. 상품 설명 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            productDescriptionImageList
                    .forEach(productDescriptionImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, DirectoryConstants.PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            //7. Product option 저장
            productOptionService.addProductOption(productId, dto.getProductOptions());

            // 8. Product detail 저장
            productDetailInfoService.addProductDetailInfo(productId, dto.getProductDetail());

            // 9. ProductDeliveryTime 저장
            deliveryTimeService.addProductDeliveryTime(productId, dto.getDeliveryTime());

            // 10. 상품 클레임 정보 저장
            productClaimService.addProductClaimInfo(productId, dto.getClaim());

            // 11. TemporaryProduct 삭제
            if (dto.isDoesUseTemporaryProduct()) {
                temporaryProductService.deleteTemporaryProduct(userId);
            }

            return ProductDTO.toDTO(newProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     * @param productImageList
     * @param categoryId
     * @param productDescriptionImageList
     */
    public void validateProductRequest(
            List<MultipartFile> productImageList,
            Long categoryId,
            List<MultipartFile> productDescriptionImageList
    ) {
        // 1. 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (productImageList.size() > 5) {
            throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        for (MultipartFile productImage : productImageList) {
            if (productImage.getSize() > FileSizeConstants.PRODUCT_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 2. 상품 설명 이미지 크기 확인
        for (MultipartFile productImage : productDescriptionImageList) {
            if (productImage.getSize() > FileSizeConstants.PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 3. 상품 내부 데이터 확인
        if (!subCategoryService.existsBySubCategoryId(categoryId)) {
            throw new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY);
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
                .orElseThrow(() -> new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT));
    }

    /**
     * productId로 삭제되지 않은 Product를 찾는 함수
     *
     * @param productId
     * @return
     */
    public Product findProductByIdAndIsDeletedFalse(Long productId) {
        return productRepository.findByIsDeletedFalseAndId(productId)
                .orElseThrow(() -> new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT));
    }

    /**
     * 판매자 다중 삭제 함수
     * - 판매자인 경우, 판매자가 등록한 상품만 삭제 가능
     * - 관리자인 경우, 모든 상품 삭제 가능
     *
     * @param productIdList
     */
    @Transactional
    public void deleteAllProduct(Long userId, List<Long> productIdList) {
        try {
            // 1. 권한 확인
            UserType role = SecurityUtils.getCurrentUserType();

            // 1-1. 판매자인 경우, 판매자 등록 상품인지 확인
            if (role == UserType.ROLE_APPROVED_SELLER && !verifySellerProductIds(userId, productIdList)) {
                throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
            }

            // 2. 상품 삭제
            productIdList
                    .forEach(this::deleteProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * productIdList에 존재하는 모든 상품들이 userId인 판매자의 상품인지 확인하는 경우
     * - true: 모두 판매자의 상품인 경우
     * - false: 판매자가 등록하지 않은 상품이 존재하는 경우
     *
     * @param userId
     * @param productIdList
     * @return
     */
    private boolean verifySellerProductIds(Long userId, List<Long> productIdList) {
        return productRepository.checkIsSellerProductIds(userId, productIdList);
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
            attachFileService.deleteAttachFileByReferencedId(productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);

            // 4. ProductDescription 삭제
            productDescriptionService.deleteProductDescription(productDescription);

            // 5. Product 대표 이미지 삭제
            attachFileService.deleteAttachFileByReferencedId(deleteProduct.getId(), ReferencedEntityType.PRODUCT);

            // 6. 찜 데이터 삭제
            wishlistRepository.deleteByProductId(productId);

            // 2. 삭제
            productRepository.deleteById(productId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 등록된 상품 정보를 수정하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @return
     */
    @Transactional
    public ProductDTO updateProduct(
            List<MultipartFile> productImageList,
            UpdateProductDTO dto,
            List<MultipartFile> productDescriptionImageList) {
        try {
            Long productId = dto.getProductId();

            // 1. Product 찾기
            Product product = findProductById(productId);

            // 2. productRequest 데이터 유효성 검사
            validateProductRequest(
                    productImageList, dto.getCategoryId(), productDescriptionImageList
            );

            // 3. Product 수정
            product.setProduct(dto);
            productRepository.save(product);

            // 4. 대표 이미지 저장 및 AttachFileGroup에 연관 관계 매핑 객체 생성
            attachFileService.deleteAttachFileByReferencedId(product.getId(), ReferencedEntityType.PRODUCT);
            productImageList
                    .forEach(productImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productImage, DirectoryConstants.PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            // 5. Product description 수정
            ProductDescription productDescription = productDescriptionService.findProductDescriptionByProductId(product.getId());
            productDescription.setDescription(dto.getDescription());

            // 6. 상품 설명 이미지 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            attachFileService.deleteAttachFileByReferencedId(productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
            productDescriptionImageList
                    .forEach(productDescriptionImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, DirectoryConstants.PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            //8. Product option 수정
            productOptionService.updateProductOptionList(productId, dto.getProductOptions());

            // 9. Product detail 수정
            ProductDetailInfo productDetailInfo = productDetailInfoService.findProductDetailInfoByProductId(product.getId());
            productDetailInfo.setProductDetailInfo(dto.getProductDetail());

            // 10. Product delivery time 수정
            deliveryTimeService.updateProductDeliveryTime(productId, dto.getDeliveryTime());

            return ProductDTO.toDTO(product);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }


    /**
     * 전체 상품 조회하는 함수
     *
     * @param subCategoryId
     * @param pageable
     * @return
     */
    public Slice<ProductForAppDTO> findProductByCategoryForApp(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    ) {
        try {
            if (subCategoryId != null && !subCategoryService.existsBySubCategoryId(subCategoryId)) {
                throw new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY);
            }

            return productRepository.findAllProductBySubCategoryId(userId, subCategoryId, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 최근 본 상품 목록 조회 함수
     *
     * @param pageable
     * @return
     */
    public Slice<ProductForAppDTO> findProductForRecentViews(
            Long userId,
            Pageable pageable
    ) {
        try {
            List<Long> productIds = recentProductViewsService.findProductIdsByUserId(userId, pageable);
            return productRepository.findAllProductByProductIds(userId, productIds, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 상품 조회 함수
     * - 판매자인 경우, 판매자의 브랜드 등록 상품 조회
     * - 관리자인 경우, 등록되어 있는 모든 상품 조회 가능
     *
     * @param userId
     * @param keyword 검색어 (null/공백: 전체 반환, not null: keyword가 존재하는 데이터 반환)
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    public Page<ProductForWebDTO> findProductForWeb(
            Long userId,
            UserType userType,
            String keyword,
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable
    ) {
        try {
            // 1. seller id 조회 (관리자인 경우 null)
            Long sellerId = getSellerId(userId, userType);

            // 2. 상품 조회
            return productRepository.findAllProduct(sellerId, keyword, startAt, endAt, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 아이디 조회 함수
     * user가 판매자인 경우, sellerId를 관리자인 경우 null을 반환하는 함수
     *
     * @param userId
     * @param userType
     * @return
     */
    private Long getSellerId(Long userId, UserType userType) {
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            Seller seller = sellerService.findSellerByUserId(userId);
            return seller.getId();
        } else {
            return null;
        }
    }

    /***
     * 상품에 대한 전체 상세 정보를 조회하는 함수
     *
     * @param productId
     * @return
     */
    public DetailedProductDTO findDetailedProduct(Long userId, Long productId) {
        try {
            // 1. productId 존재확인
            findProductById(productId);

            // 2. Product 세부 데이터 가져오기
            DetailedProductDTO detailedProductDTO = productRepository.findProductByProductId(userId, productId);

            // 3. Product 대표 이미지 리스트 가져오기
            List<AttachFileDTO> attachFileDTOS = attachFileService.findAllAttachFileByReferencedId(productId, ReferencedEntityType.PRODUCT);
            detailedProductDTO.setProductImageList(attachFileDTOS);

            // 4. 최근 본 상품 저장
            recentProductViewsService.addRecentProductView(userId, productId);

            return detailedProductDTO;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 웹에서 상품 전체 정보를 조회하는 함수
     * - 판매자: 판매자의 브랜드가 등록한 상품만 조회 가능
     * - 관리자: 모든 상품 조회 가능
     *
     * @param userId
     * @param productId
     * @return
     */
    public ProductDetailForWebDTO findProductDetailForWeb(Long userId, Long productId) {
        try {
            UserType userType = SecurityUtils.getCurrentUserType();
            Long sellerId = userType == UserType.ROLE_APPROVED_SELLER ? sellerService.findSellerByUserId(userId).getId() : null;

            // 1. 데이터 조회
            ProductDetailForWebDTO dto = productRepository.findProductDetailByProductId(sellerId, userType, productId);

            // 2. 판매자의 상품인지 확인
            if (dto == null) {
                throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
            }

            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
