package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.redis.service.RecentProductViewsService;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadProductService {
    private final ProductRepository productRepository;
    private final ReadSellerService readSellerService;
    private final AttachFileService attachFileService;
    private final SubCategoryService subCategoryService;
    private final RecentProductViewsService recentProductViewsService;

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     *
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
     * productIdList에 존재하는 모든 상품들이 userId인 판매자의 상품인지 확인하는 경우
     * - true: 모두 판매자의 상품인 경우
     * - false: 판매자가 등록하지 않은 상품이 존재하는 경우
     *
     * @param userId
     * @param productIdList
     * @return
     */
    public boolean verifySellerProductIds(Long userId, List<Long> productIdList) {
        return productRepository.checkIsSellerProductIds(userId, productIdList);
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
     * @param keyword  검색어 (null/공백: 전체 반환, not null: keyword가 존재하는 데이터 반환)
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
            Seller seller = readSellerService.findSellerByUserId(userId);
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
            Long sellerId = userType == UserType.ROLE_APPROVED_SELLER ? readSellerService.findSellerByUserId(userId).getId() : null;

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
