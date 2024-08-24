package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.product.history.ProductHistoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateProductService {
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;
    private final ReadSellerService readSellerService;
    private final AttachFileService attachFileService;
    private final TemporaryProductService temporaryProductService;
    private final ProductDeliveryTimeService deliveryTimeService;
    private final ProductClaimService productClaimService;
    private final ProductHistoryService productHistoryService;
    private final ReadProductService readProductService;

    /**
     * 새로운 Product 생성 함수
     * - 판매자인 경우, 요청한 판매자의 브랜드로 상품 등록
     * - 관리자인 경우, request body 의 sellerId의 브랜드로 상품 등록
     *
     * @param dto
     * @return
     */
    @Transactional
    public ProductDTO addProduct(
            Long userId,
            CreateProductDTO dto) {
        try {
            // 0. 판매자 id 유효성 검사
            // 판매자: API 요청 시, 사용한 인증 정보의 userId를 통해 sellerId 반환
            // 관리자: dto 에 sellerId 존재하는지 확인 후, 존재하는 sellerId 인지 확인
            UserType userType = SecurityUtils.getCurrentUserType();
            Long sellerId = null;
            if (userType == UserType.ROLE_APPROVED_SELLER) {
                Seller seller = readSellerService.findSellerByUserId(userId);
                sellerId = seller.getId();
            } else {
                sellerId = dto.getSellerId();
                if (sellerId == null || !readSellerService.existsSellerBySellerId(sellerId)) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "sellerId 정보가 잘 못 되었습니다. 존재하지 않는 판매자 입니다.");
                }
            }

            // 1. productRequest 데이터 유효성 검사
            readProductService.validateProductRequest(
                    dto.getProductImages(), dto.getCategoryId()
            );
            readProductService.validateDeliveryRefundFee(
                    dto.getDeliveryFee(),
                    dto.getRefundFee(),
                    dto.getSpecialDeliveryFee(),
                    dto.getSpecialRefundFee(),
                    dto.getDeliveryFeeType(),
                    dto.getRefundFeeType()
            );

            // 2. 상풍 번호 생성
            String productNumber = StringUtils.getRandomUniqueNumber();

            // 3. Product 저장
            // 배송비 & 반송비는 CHARGE_UNDER_30000 일 때만 저장
            Product newProduct = productRepository.save(dto.toEntity(productNumber, sellerId));
            Long productId = newProduct.getId();

            // 4. Product option 저장
            productOptionService.addProductOption(productId, dto.getProductOptions());

            // 5. Product detail 저장
            productDetailInfoService.addProductDetailInfo(productId, dto.getProductDetail());

            // 6. ProductDeliveryTime 저장
            deliveryTimeService.addProductDeliveryTime(productId, dto.getDeliveryTime());

            // 7. 상품 클레임 정보 저장
            productClaimService.addProductClaimInfo(productId, dto.getClaim());

            // 8. TemporaryProduct 삭제
            if (dto.isDoesUseTemporaryProduct()) {
                temporaryProductService.deleteTemporaryProduct(userId);
            }

            // 9. 상품 관련 이력 생성
            addProductHistoryInCreateMode(newProduct);

            return ProductDTO.toDTO(newProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 상품 관련 이력 생성 (상품 이력)
     *
     * @param product
     */
    @Transactional
    public void addProductHistoryInCreateMode(
            Product product
    ) {
        // 1. 상품 이력 생성
        ProductHistory productHistory = ProductHistory.toEntity(product);
        productHistoryService.saveProductHistory(productHistory);
    }
}
