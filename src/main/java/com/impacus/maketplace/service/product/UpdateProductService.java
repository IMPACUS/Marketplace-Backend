package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.product.history.ProductHistoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateProductService {
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;
    private final ReadSellerService readSellerService;
    private final ProductDeliveryTimeService deliveryTimeService;
    private final ProductClaimService productClaimService;
    private final ProductHistoryService productHistoryService;
    private final ReadProductService readProductService;
    private final EntityManager entityManager;

    /**
     * 등록된 상품 정보 수정 함수
     * - 판매자: 판매자의 브랜드인 상품만 수정가능
     * - 관리자: 모든 상품 수정 가능
     *
     * @return
     */
    @Transactional
    public ProductDTO updateProduct(
            Long userId,
            Long productId,
            UpdateProductDTO dto,
            boolean isOverwrite
    ) {
        try {
            // 1. Product 찾기
            CommonProductDTO savedProduct = productRepository.findCommonProductByProductId(productId);

            // 2. (요청한 사용자가 판매자인 경우) 판매자가 등록한 상품인지 확인
            // - 판매자가 등록한 상품이 아닌 경우 에러 발생 시킴
            validateUserAccess(userId, savedProduct, dto);

            // 3. productRequest 유효성 검사
            validateProductRequest(dto, savedProduct);

            // 4. 상품 이력 저장 (조건에 부합하는 경우)
            addProductHistoryInUpdateMode(savedProduct, dto, savedProduct.getProductImages());

            // 5. Product 수정
            Product changedProduct = applyProductChanges(savedProduct, dto, isOverwrite);

            entityManager.flush();

            // 6. 연관된 Product 정보 수정
            updateProductRelatedInfo(productId, dto);

            return ProductDTO.toDTO(changedProduct);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new CustomException(HttpStatus.CONFLICT, ProductErrorType.PRODUCT_CONCURRENT_MODIFICATION);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 상품에 사용자가 접근 가능 확인
     *
     * @param userId
     * @param savedProduct
     * @param dto
     */
    private void validateUserAccess(Long userId, CommonProductDTO savedProduct, UpdateProductDTO dto) {
        // 현재 사용자의 유형을 가져옴
        UserType userType = SecurityUtils.getCurrentUserType();
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            // 판매자일 경우, 본인의 상품인지 확인
            Long sellerId = readSellerService.findSellerIdByUserId(userId);
            if (!sellerId.equals(savedProduct.getSellerId())) {
                throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
            }
            // 판매자는 판매 수수료를 수정할 수 없음
            if (dto.getSalesChargePercent() != null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "판매자는 판매 수수료를 수정할 수 없습니다.");
            }
        }
    }

    /**
     * 상품 수정 요청 데이터 유효성 검사
     *
     * @param dto
     * @param savedProduct
     */
    private void validateProductRequest(UpdateProductDTO dto, CommonProductDTO savedProduct) {
        // 상품 이미지, 카테고리, 묶음 배송 그룹 유효성 검사
        readProductService.validateProductRequest(
                savedProduct.getSellerId(),
                dto
        );
    }

    /**
     * 상품 이력 생성
     *
     * @param nowProduct    현재 DB에 저장된 상품
     * @param newProduct    변경 예정 상품 정보
     * @param productImages 변경된 상품 이미지
     */
    @Transactional
    public void addProductHistoryInUpdateMode(
            CommonProductDTO nowProduct,
            UpdateProductDTO newProduct,
            List<String> productImages
    ) {
        // (상품 이미지가 변경된 경우) 상품 이력 저장
        if (!newProduct.getName().equals(nowProduct.getName())) {
            ProductHistory productHistory = ProductHistory.toEntity(nowProduct.getProductId(), newProduct.getName(), productImages);
            productHistoryService.saveProductHistory(productHistory);
        }
    }

    /**
     * 상품 수정
     *
     * @param savedProduct
     * @param dto
     * @param isOverwrite
     * @return
     */
    @Transactional
    public Product applyProductChanges(CommonProductDTO savedProduct, UpdateProductDTO dto, boolean isOverwrite) {
        // Product 수정 사항 적용
        Product changedProduct = savedProduct.toEntity(dto);
        if (isOverwrite) {
            // 덮어씌기를 허용하는 경우, 낙관적 락 에러가 나지 않도록 version 수동 증가
            changedProduct.setVersion(savedProduct.getVersion());
        }
        return productRepository.save(changedProduct);
    }

    /**
     * 상품 연관 관계 데이터 수정
     *
     * @param productId
     * @param dto
     */
    @Transactional
    public void updateProductRelatedInfo(Long productId, UpdateProductDTO dto) {
        // 상품 옵션
        productOptionService.updateProductOptionList(productId, dto.getProductOptions());

        // 상품 상세 정보
        ProductDetailInfo productDetailInfo = productDetailInfoService.findProductDetailInfoByProductId(productId);
        productDetailInfo.setProductDetailInfo(dto.getProductDetail());

        // 상품 배송 시간
        deliveryTimeService.updateProductDeliveryTime(productId, dto.getDeliveryTime());

        // 상품 클레임 정보
        productClaimService.updateProductClaimInfo(productId, dto.getClaim());
    }

    /**
     * 등록된 상품 이미지 수정 함수
     * - 판매자: 판매자의 브랜드인 상품만 수정가능
     * - 관리자: 모든 상품 수정 가능
     *
     * @return
     */
    @Transactional
    public void updateProductImages(Long userId, Long productId, List<String> productImages) {
        try {
            Product product = readProductService.findProductById(productId);

            // 1. (요청한 사용자가 판매자인 경우) 판매자가 등록한 상품인지 확인
            // - 판매자가 등록한 상품이 아닌 경우 에러 발생 시킴
            UserType userType = SecurityUtils.getCurrentUserType();
            if (userType == UserType.ROLE_APPROVED_SELLER) {
                Seller seller = readSellerService.findSellerByUserId(userId);
                if (!seller.getId().equals(product.getSellerId())) {
                    throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
                }
            }

            // 2. 이미지 업데이트
            productRepository.updateProductImagesById(productId, productImages);

            // 3. 상품 이력 저장
            ProductHistory productHistory = ProductHistory.toEntity(product.getId(), product.getName(), productImages);
            productHistoryService.saveProductHistory(productHistory);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
