package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
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
import com.impacus.maketplace.service.api.ProductInterface;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadProductService implements ProductInterface {
    private final ProductRepository productRepository;
    private final ReadSellerService readSellerService;
    private final AttachFileService attachFileService;
    private final SubCategoryService subCategoryService;
    private final RecentProductViewsService recentProductViewsService;

    @Override
    public void checkExistenceById(long productId) {
        if (!productRepository.existsByIsDeletedFalseAndId(productId)) {
            throw new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT);
        }
    }

    @Override
    public void validateProductRequest(
            List<String> productImages,
            Long categoryId
    ) {
        // 1. 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (productImages.size() > 5) {
            throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

//        for (MultipartFile productImage : productImages) {
//            if (productImage.getSize() > FileSizeConstants.PRODUCT_IMAGE_SIZE_LIMIT) {
//                throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
//            }
//        }

        // 2. 상품 내부 데이터 확인
        if (!subCategoryService.existsBySubCategoryId(categoryId)) {
            throw new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY);
        }
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findByIsDeletedFalseAndId(productId)
                .orElseThrow(() -> new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT));
    }

    @Override
    public boolean verifySellerProductIds(Long userId, List<Long> productIdList) {
        return productRepository.checkIsSellerProductIds(userId, productIdList);
    }

    @Override
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

    @Override
    public Slice<ProductForAppDTO> findProductForRecentViews(
            Long userId,
            Pageable pageable
    ) {
        try {
            List<Long> productIds = recentProductViewsService.findProductIdsByUserId(userId, pageable);
            return productRepository.findProductsByProductIds(userId, productIds, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    @Override
    public Page<ProductForWebDTO> findProductsForWeb(
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
            return productRepository.findProductsForWeb(sellerId, keyword, startAt, endAt, pageable);
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

    @Override
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

    @Override
    public ProductDetailForWebDTO findProductDetailForWeb(Long userId, Long productId) {
        try {
            UserType userType = SecurityUtils.getCurrentUserType();
            Long sellerId = userType == UserType.ROLE_APPROVED_SELLER ? readSellerService.findSellerByUserId(userId).getId() : null;

            // 존재하는 상품인지 확인상품
            checkExistenceById(productId);

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

    /**
     * 배송비&반품비 유효성 검사 함수
     * - CHARGE_UNDER_30000 일 때, 배송비 정보 혹은 반품비 정보가 null 일 수 없음.
     *
     * @param deliveryFee
     * @param refundFee
     * @param specialDeliveryFee
     * @param specialRefundFee
     * @param deliveryFeeType
     * @param refundFeeType
     */
    public void validateDeliveryRefundFee(
            Integer deliveryFee,
            Integer refundFee,
            Integer specialDeliveryFee,
            Integer specialRefundFee,
            DeliveryRefundType deliveryFeeType,
            DeliveryRefundType refundFeeType
    ) {
        // 1. 배송비 정보 확인
        if (deliveryFeeType == DeliveryRefundType.CHARGE_UNDER_30000 && (deliveryFee == null || specialDeliveryFee == null)) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA,
                    "deliveryFeeType 가 CHARGE_UNDER_30000 일 때는 배송비 데이터가 null 이면 안됩니다.");

        }

        // 2. 반송비 정보 확인
        if (refundFeeType == DeliveryRefundType.CHARGE_UNDER_30000 && (refundFee == null || specialRefundFee == null)) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA,
                    "refundFeeType 가 CHARGE_UNDER_30000 일 때는 반송비 데이터가 null 이면 안됩니다.");

        }
    }
}
