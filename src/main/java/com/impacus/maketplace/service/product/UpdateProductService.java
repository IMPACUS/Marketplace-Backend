package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.product.history.ProductHistoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
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
            UpdateProductDTO dto) {
        try {
            // 1. Product 찾기
            Product product = readProductService.findProductById(productId);

            // 2. (요청한 사용자가 판매자인 경우) 판매자가 등록한 상품인지 확인
            // - 판매자가 등록한 상품이 아닌 경우 에러 발생 시킴
            UserType userType = SecurityUtils.getCurrentUserType();

            if (userType == UserType.ROLE_APPROVED_SELLER) {
                Long sellerId = readSellerService.findSellerIdByUserId(userId);
                if (!sellerId.equals(product.getSellerId())) {
                    throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
                }
                if (dto.getSalesChargePercent() != null) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "판매자는 판매 수수료를 수정할 수 없습니다.");
                }
            }

            // 3. productRequest 데이터 유효성 검사
            readProductService.validateProductRequest(
                    product.getProductImages(),
                    dto.getCategoryId(),
                    product.getSellerId(),
                    dto.getBundleDeliveryOption(),
                    dto.getBundleDeliveryGroupId()
            );
            readProductService.validateDeliveryRefundFee(
                    dto.getDeliveryFee(),
                    dto.getRefundFee(),
                    dto.getSpecialDeliveryFee(),
                    dto.getSpecialRefundFee(),
                    dto.getDeliveryFeeType(),
                    dto.getRefundFeeType()
            );

            // 5. 상품 이력 저장 (조건에 부합하는 경우)
            addProductHistoryInUpdateMode(product, dto, product.getProductImages());

            // 6. Product 수정
            product.setProduct(dto);
            productRepository.save(product);

            // 7. Product option 수정
            productOptionService.updateProductOptionList(productId, dto.getProductOptions());

            // 8. Product detail 수정
            ProductDetailInfo productDetailInfo = productDetailInfoService.findProductDetailInfoByProductId(product.getId());
            productDetailInfo.setProductDetailInfo(dto.getProductDetail());

            // 9. Product delivery time 수정
            deliveryTimeService.updateProductDeliveryTime(productId, dto.getDeliveryTime());

            // 10. 상품 클레임 정보 수정
            productClaimService.updateProductClaimInfo(productId, dto.getClaim());

            return ProductDTO.toDTO(product);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 상품 이력 생성
     *
     * @param nowProduct    현재 DB에 저장된 상품
     * @param newProduct    변경 예정 상품 정보
     * @param productImages 변경된 상품 이미지
     * @prarm newProduct
     */
    public void addProductHistoryInUpdateMode(
            Product nowProduct,
            UpdateProductDTO newProduct,
            List<String> productImages
    ) {
        // (상품 이미지가 변경된 경우) 상품 이력 저장
        if (!newProduct.getName().equals(nowProduct.getName())) {
            ProductHistory productHistory = ProductHistory.toEntity(nowProduct.getId(), newProduct.getName(), productImages);
            productHistoryService.saveProductHistory(productHistory);
        }
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
