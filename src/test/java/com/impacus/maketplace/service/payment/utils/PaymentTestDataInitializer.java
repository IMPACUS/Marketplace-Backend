package com.impacus.maketplace.service.payment.utils;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.SellerType;
import com.impacus.maketplace.dto.product.request.*;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.repository.product.history.ProductOptionHistoryRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentTestDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionHistoryRepository productOptionHistoryRepository;

    public void initializeTestData() {
        // 1. 기존 데이터 삭제
        userRepository.deleteAll();
        sellerRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
        productRepository.deleteAll();
        productOptionRepository.deleteAll();
        productOptionHistoryRepository.deleteAll();
        // 2. 테스트용 데이터 생성

        // 2.1 판매자 생성
        Seller seller = Seller.builder()
                .userId(1L)
                .contactName("테스트용 판매자")
                .marketName("테스트마켓")
                .logoImageId(1L)
                .customerServiceNumber("000-0000-0000")
                .businessType(BusinessType.SOLE_PROPRIETOR)
                .sellerType(SellerType.BRAND)
                .build();

        sellerRepository.save(seller);
        // 2.2 상품 옵션 생성 (상품별로 유효 옵션 1개, 삭제된 옵션 1개)

        // 2.3 판매자의 상품 등록 (유효한 상품 3개 (일반 2개, 그린 1개), 삭제된 상품 1개, 재고 부족 1개, 판매 중 상태 아닌 상품 1개)

        // 2.4 싱품 옵션 히스토리 생성 (상품 옵션별로 1개씩)

        // 2.5 사용자(소비자) 생성

        // 2.6 포인트 지급 (10000)

        // 2.7 쿠폰 등록(혜택 구분 2개, 적용 타입 2개, 쿠폰 사용 범위 2개, 사용 가능 기준 금액 2개)

        // 2.8 등록한 쿠폰을 이용해서 사용자 쿠폰 생성

        // 2.9 사용자 쿠폰 사용 가능한 상태로 설정
    }
    private CreateProductDTO createProductDTO(String name, Integer marketPrice, Integer appSalesPrice, Integer discountPrice, ProductStatus productStatus, ProductType productType, CreateProductOptionDTO createProductOptionDTO) {
        CreateProductDetailInfoDTO productDetail = new CreateProductDetailInfoDTO(
                "productType", "productMaterial", "productColor", "productSize", "dateOfManufacture",
                "washingPrecautions", "countryOfManufacture", "manufacturer", "importer", "quantityAssuranceStandards",
                "asManager", "contactNumber");

        CreateProductDeliveryTimeDTO productDeliveryTimeDTO = new CreateProductDeliveryTimeDTO(0, 0);

        CreateClaimInfoDTO createClaimInfoDTO = new CreateClaimInfoDTO();

        // Create list of product images and add an image URL
        List<String> productImages = new ArrayList<>();
        productImages.add("ImageURL");

        // Create list of product options and add the provided option DTO
        List<CreateProductOptionDTO> productOptions = new ArrayList<>();
        productOptions.add(createProductOptionDTO);

        // Create and return the CreateProductDTO object
        return new CreateProductDTO(
                1L,                       // sellerId
                false,                    // doesUseTemporaryProduct
                name,                     // name
                DeliveryType.GENERAL_DELIVERY, // deliveryType
                false,                    // isCustomProduct
                1L,                       // categoryId
                DeliveryRefundType.FREE_SHIPPING, // deliveryFeeType
                DeliveryRefundType.FREE_SHIPPING, // refundFeeType
                0,                        // deliveryFee
                0,                        // refundFee
                0,                        // specialDeliveryFee
                0,                        // specialRefundFee
                DeliveryCompany.CJ,       // deliveryCompany
                BundleDeliveryOption.INDIVIDUAL_SHIPPING_ONLY, // bundleDeliveryOption
                null,                     // bundleDeliveryGroupId
                productImages,            // productImages
                marketPrice,              // marketPrice
                appSalesPrice,            // appSalesPrice
                discountPrice,            // discountPrice
                10,                       // weight
                productStatus,            // productStatus
                "테스트 상품",            // description
                productType,              // type
                null,                     // salesChargePercent
                productDetail,            // productDetail
                productOptions,           // productOptions
                productDeliveryTimeDTO,   // deliveryTime
                createClaimInfoDTO        // claim
        );
    }

    private CreateProductOptionDTO createProductOptionDTO(Long id, Long stock) {
        return new CreateProductOptionDTO(id, "color", "size", stock);
    }

}
