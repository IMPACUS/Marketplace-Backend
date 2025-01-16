package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.redis.service.SearchProductService;
import com.impacus.maketplace.repository.product.ProductRepository;
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
    private final TemporaryProductService temporaryProductService;
    private final ProductDeliveryTimeService deliveryTimeService;
    private final ProductClaimService productClaimService;
    private final ProductHistoryService productHistoryService;
    private final ReadProductService readProductService;
    private final SearchProductService searchProductService;

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
            CreateProductDTO dto
    ) {
        try {
            // 1. 유효성 검사
            Long sellerId = getSellerId(userId, dto.getSellerId());
            readProductService.validateProductImages(dto.getProductImages());
            readProductService.validateProductRequest(
                    sellerId,
                    dto
            );

            // 2. Product 저장
            Product newProduct = saveProduct(sellerId, dto);

            // 3. TemporaryProduct 삭제
            if (dto.isDoesUseTemporaryProduct()) {
                temporaryProductService.deleteTemporaryProduct(userId);
            }

            // 4. 상품 관련 이력 생성
            addProductHistoryInCreateMode(newProduct);

            // 5. 검색어 데이터 저장
            addProductSearchData(newProduct);

            return ProductDTO.toDTO(newProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 상품 저장
     *
     * @param sellerId 상품이 등록될 브랜드의 판매자 ID
     * @param dto 상품 등록 요청 정보
     * @return
     */
    @Transactional
    public Product saveProduct(
            Long sellerId,
            CreateProductDTO dto
    ) {
        Product newProduct = productRepository.save(dto.toEntity(sellerId));
        Long productId = newProduct.getId();

        // Product option 저장
        productOptionService.addProductOption(productId, dto.getProductOptions());

        // Product detail 저장
        productDetailInfoService.addProductDetailInfo(productId, dto.getProductDetail());

        // ProductDeliveryTime 저장
        deliveryTimeService.addProductDeliveryTime(productId, dto.getDeliveryTime());

        // 상품 클레임 정보 저장
        productClaimService.addProductClaimInfo(productId, dto.getClaim());
        
        return newProduct;
    }

    /**
     * 판매자 ID 가져오기
     * - 판매자: API 요청 시, 사용한 인증 정보의 userId를 통해 sellerId 반환
     * - 관리자: dto 에 sellerId 존재하는지 확인 후, 존재하는 sellerId 인지 확인
     * @param userId
     * @param sellerId
     * @return
     */
    private Long getSellerId(Long userId, Long sellerId) {
        // TODO 관리자인 경우 sellerId가 인증된 사용자인지 확인
        UserType userType = SecurityUtils.getCurrentUserType();
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            return readSellerService.findSellerIdByUserId(userId);
        } else {
            if (sellerId == null || !readSellerService.existsSellerBySellerId(sellerId)) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "sellerId 정보가 잘 못 되었습니다. 존재하지 않는 판매자 입니다.");
            }

            return sellerId;
        }
    }

    /**
     *  상품 검색어 정보 저장
     *
     * @param product SearchData 가 참조할 상품
     */
    @Transactional
    public void addProductSearchData(Product product) {
        try {
            searchProductService.addSearchData(
                    SearchType.PRODUCT,
                    product.getId(),
                    product.getName()
            );
        } catch (Exception ex) {
            LogUtils.writeErrorLog("addProduct", "Fail to save search data", ex);
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
