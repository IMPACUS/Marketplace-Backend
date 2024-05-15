package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryEnum;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductEnum;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDescription;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.repository.product.ShoppingBasketRepository;
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

    private static final int PRODUCT_IMAGE_SIZE_LIMIT = 341172; // (1080 * 1053 * 3 = 3.41172MB 341172byte)
    private static final int PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT = 341172; // (1000 * 8000 * 3 = 24MB)
    private static final String PRODUCT_IMAGE_DIRECTORY = "productImage";
    private static final String PRODUCT_DESCRIPTION_IMAGE_DIRECTORY = "productDescriptionImage";
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;
    private final SellerService sellerService;
    private final AttachFileService attachFileService;
    private final ProductDescriptionService productDescriptionService;
    private final TemporaryProductService temporaryProductService;
    private final ObjectCopyHelper objectCopyHelper;
    private final SubCategoryService subCategoryService;
    private final WishlistRepository wishlistRepository;
    private final ShoppingBasketRepository shoppingBasketRepository;

    /**
     * 새로운 Product 생성 함수
     *
     * @param productRequest
     * @return
     */
    @Transactional
    public ProductDTO addProduct(
            Long userId,
            List<MultipartFile> productImageList,
            CreateProductDTO productRequest,
            List<MultipartFile> productDescriptionImageList) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);

            // 1. productRequest 데이터 유효성 검사
            validateProductRequest(
                    productImageList, productRequest.getCategoryId(), productDescriptionImageList
            );

            // 2. 상풍 번호 생성
            String productNumber = StringUtils.getProductNumber();

            // 3. Product 저장
            Product newProduct = productRepository.save(productRequest.toEntity(productNumber, seller.getId()));
            Long productId = newProduct.getId();

            // 4. 대표 이미지 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            productImageList
                    .forEach(productImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productImage, PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            // 5. Product description 저장
            ProductDescription productDescription = productDescriptionService.addProductDescription(productId, productRequest);

            // 6. 상품 설명 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            productDescriptionImageList
                    .forEach(productDescriptionImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            //7. Product option 저장
            productOptionService.addProductOption(productId, productRequest.getProductOptions());

            // 8. Product detail 저장
            productDetailInfoService.addProductDetailInfo(productId, productRequest.getProductDetail());

            // 9. TemporaryProduct 삭제
            if (productRequest.isDoesUseTemporaryProduct()) {
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
            throw new CustomException(ProductEnum.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        for (MultipartFile productImage : productImageList) {
            if (productImage.getSize() > PRODUCT_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ProductEnum.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 2. 상품 설명 이미지 크기 확인
        for (MultipartFile productImage : productDescriptionImageList) {
            if (productImage.getSize() > PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT) {
                throw new CustomException(ProductEnum.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 3. 상품 내부 데이터 확인
        if (!subCategoryService.existsBySubCategoryId(categoryId)) {
            throw new CustomException(CategoryEnum.NOT_EXISTED_SUB_CATEGORY);
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
                .orElseThrow(() -> new CustomException(ProductEnum.NOT_EXISTED_PRODUCT));
    }

    /**
     * productId로 삭제되지 않은 Product를 찾는 함수
     *
     * @param productId
     * @return
     */
    public Product findProductByIdAndIsDeletedFalse(Long productId) {
        return productRepository.findByIsDeletedFalseAndId(productId)
                .orElseThrow(() -> new CustomException(ProductEnum.NOT_EXISTED_PRODUCT));
    }

    @Transactional
    public void deleteAllProduct(List<Long> productIdList) {
        try {
            productIdList
                    .forEach(this::deleteProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
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
                            attachFileService.uploadFileAndAddAttachFile(productImage, PRODUCT_IMAGE_DIRECTORY, productId, ReferencedEntityType.PRODUCT);
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
                            attachFileService.uploadFileAndAddAttachFile(productDescriptionImage, PRODUCT_DESCRIPTION_IMAGE_DIRECTORY, productDescription.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            //8. Product option 수정
            productOptionService.updateProductOptionList(productId, dto.getProductOptions());

            // 9. Product detail 수정
            ProductDetailInfo productDetailInfo = productDetailInfoService.findProductDetailInfoByProductId(product.getId());
            productDetailInfo.setProductDetailInfo(dto.getProductDetail());

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
                throw new CustomException(CategoryEnum.NOT_EXISTED_SUB_CATEGORY);
            }

            return productRepository.findAllProductBySubCategoryId(userId, subCategoryId, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자인 경우, 판매자 등록 상품만 관리자인 경우 전체 상품 조회하는 함수
     *
     * @param userId
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    public Page<ProductForWebDTO> findProductForWeb(Long userId, LocalDate startAt, LocalDate endAt, Pageable pageable) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);
            return productRepository.findAllProduct(seller.getId(), startAt, endAt, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /***
     * 상품에 대한 전체 상세 정보를 조회하는 함수
     *
     * @param productId
     * @return
     */
    public DetailedProductDTO findDetailedProduct(Long productId) {
        try {
            // 1. productId 존재확인
            findProductById(productId);

            // 2. Product 세부 데이터 가져오기
            DetailedProductDTO detailedProductDTO = productRepository.findProductByProductId(productId);

            // 3. Product 대표 이미지 리스트 가져오기
            List<AttachFileDTO> attachFileDTOS = attachFileService.findAllAttachFileByReferencedId(productId, ReferencedEntityType.PRODUCT);
            detailedProductDTO.setProductImageList(attachFileDTOS);

            return detailedProductDTO;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자용 웹에서 상품 전체 정보를 조회하는 함수
     *
     * @param userId
     * @param productId
     * @return
     */
    public ProductDetailForWebDTO findProductDetailForWeb(Long userId, Long productId) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);

            Product product = findProductById(productId);
            ProductDetailForWebDTO dto = objectCopyHelper.copyObject(product, ProductDetailForWebDTO.class);

            // 1. 판매자의 상품인지 확인
            if (!product.getSellerId().equals(seller.getId())) {
                throw new CustomException(ProductEnum.PRODUCT_ACCESS_DENIED);
            }

            // 2. TemporaryProductDescription 값 가져오기
            ProductDescription description = productDescriptionService.findProductDescriptionByProductId(productId);
            dto.setDescription(description.getDescription());

            // 3. TemporaryProductOption 값 가져오기
            List<ProductOptionDTO> options = productOptionService.findProductOptionByProductId(productId)
                    .stream()
                    .map(option -> new ProductOptionDTO(option.getId(), option.getColor(), option.getSize()))
                    .toList();
            dto.setProductOptionDTO(options);

            // 4. TemporaryProductDescription 값 가져오기
            ProductDetailInfo detailInfo = productDetailInfoService.findProductDetailInfoByProductId(productId);
            dto.setProductDetail(objectCopyHelper.copyObject(detailInfo, ProductDetailInfoDTO.class));

            // 5. 대표이미지 데이터 가져오기
            List<AttachFileDTO> attachFileDTOS = attachFileService.findAllAttachFile(description.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION)
                    .stream().map(attachFile -> new AttachFileDTO(attachFile.getId(), attachFile.getAttachFileName()))
                    .toList();
            dto.setProductImageList(attachFileDTOS);

            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
