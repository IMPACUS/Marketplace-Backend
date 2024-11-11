package com.impacus.maketplace.service.payment.utils;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.SellerType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.request.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.entity.product.history.ProductOptionHistory;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.repository.product.history.ProductOptionHistoryRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private GreenLabelPointRepository greenLabelPointRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteAllData() {
        // 데이터 삭제
        userCouponRepository.deleteAll();
        couponRepository.deleteAll();
        greenLabelPointRepository.deleteAll();
        productOptionHistoryRepository.deleteAll();
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        userRepository.deleteAll();

        // 시퀀스 초기화
        entityManager.createNativeQuery("ALTER SEQUENCE user_info_user_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE seller_seller_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE coupon_coupon_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE issued_coupon_issued_coupon_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE product_info_product_info_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE product_option_product_option_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE product_option_history_product_option_history_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE green_label_point_green_label_point_id_seq RESTART WITH 1").executeUpdate();
    }

    @Transactional
    public void initializeTestData() {
        // 1. 판매자 생성
        Seller seller = Seller.builder()
                .userId(1L)
                .contactName("테스트용 판매자")
                .marketName("테스트마켓")
                .logoImageId(1L)
                .customerServiceNumber("000-0000-0000")
                .businessType(BusinessType.SOLE_PROPRIETOR)
                .sellerType(SellerType.BRAND)
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        // 2. 상품 옵션 생성 (유효 옵션 1개, 삭제된 옵션 1개, 재고 0인 옵션 1개)
        CreateProductOptionDTO createProductOptionDTO1 = createProductOptionDTO(100L);
        CreateProductOptionDTO createProductOptionDTO2 = createProductOptionDTO(0L);

        List<CreateProductOptionDTO> createProductOptionDTOs = new ArrayList<>();
        createProductOptionDTOs.add(createProductOptionDTO1);
        createProductOptionDTOs.add(createProductOptionDTO2);

        // 3. 테스트 상품 1 생성 및 저장
        CreateProductDTO testProductDTO1 = createProductDTO("테스트 상품1", 10000, 10000, 10000, ProductStatus.SALES_PROGRESS, ProductType.GENERAL, createProductOptionDTOs);
        Product testProduct1 = testProductDTO1.toEntity(savedSeller.getId());
        Product savedProduct1 = productRepository.save(testProduct1);

        ProductOption productOption1 = testProductDTO1.getProductOptions().get(0).toEntity(savedProduct1.getId());
        ProductOption productOption2 = testProductDTO1.getProductOptions().get(1).toEntity(savedProduct1.getId());
        ProductOption productOption3 = ProductOption.builder()
                .productId(savedProduct1.getId())
                .color(testProductDTO1.getProductOptions().get(0).getColor())
                .size(testProductDTO1.getProductOptions().get(0).getSize())
                .stock(testProductDTO1.getProductOptions().get(0).getStock())
                .isDeleted(true)
                .build();

        ProductOption savedProductOption1 = productOptionRepository.save(productOption1);
        ProductOption savedProductOption2 = productOptionRepository.save(productOption2);
        ProductOption savedProductOption3 = productOptionRepository.save(productOption3);

        ProductOptionHistory productOptionHistory1 = ProductOptionHistory.toEntity(savedProductOption1);
        ProductOptionHistory productOptionHistory2 = ProductOptionHistory.toEntity(savedProductOption2);
        ProductOptionHistory productOptionHistory3 = ProductOptionHistory.toEntity(savedProductOption3);


        productOptionHistoryRepository.save(productOptionHistory1);
        productOptionHistoryRepository.save(productOptionHistory2);
        productOptionHistoryRepository.save(productOptionHistory3);

        // 4. 테스트 상품 2 생성 및 저장
        CreateProductDTO testProductDTO2 = createProductDTO("테스트 상품2", 10000, 10000, 8000, ProductStatus.SALES_PROGRESS, ProductType.GREEN_TAG, createProductOptionDTOs);
        Product testProduct2 = testProductDTO2.toEntity(savedSeller.getId());
        Product savedProduct2 = productRepository.save(testProduct2);

        ProductOption productOption4 = testProductDTO2.getProductOptions().get(0).toEntity(savedProduct2.getId());
        ProductOption productOption5 = testProductDTO2.getProductOptions().get(1).toEntity(savedProduct2.getId());
        ProductOption productOption6 = ProductOption.builder()
                .productId(savedProduct2.getId())
                .color(testProductDTO2.getProductOptions().get(0).getColor())
                .size(testProductDTO2.getProductOptions().get(0).getSize())
                .stock(testProductDTO2.getProductOptions().get(0).getStock())
                .isDeleted(true)
                .build();

        ProductOption savedProductOption4 = productOptionRepository.save(productOption4);
        ProductOption savedProductOption5 = productOptionRepository.save(productOption5);
        ProductOption savedProductOption6 = productOptionRepository.save(productOption6);

        ProductOptionHistory productOptionHistory4 = ProductOptionHistory.toEntity(savedProductOption4);
        ProductOptionHistory productOptionHistory5 = ProductOptionHistory.toEntity(savedProductOption5);
        ProductOptionHistory productOptionHistory6 = ProductOptionHistory.toEntity(savedProductOption6);


        productOptionHistoryRepository.save(productOptionHistory4);
        productOptionHistoryRepository.save(productOptionHistory5);
        productOptionHistoryRepository.save(productOptionHistory6);

        // 5. 테스트 상품 3 생성 및 저장
        CreateProductDTO testProductDTO3 = createProductDTO("테스트 상품3", 20000, 19999, 16999, ProductStatus.SALES_PROGRESS, ProductType.GREEN_TAG, createProductOptionDTOs);
        Product testProduct3 = testProductDTO3.toEntity(savedSeller.getId());
        Product savedProduct3 = productRepository.save(testProduct3);

        ProductOption productOption7 = testProductDTO3.getProductOptions().get(0).toEntity(savedProduct3.getId());
        ProductOption productOption8 = testProductDTO3.getProductOptions().get(1).toEntity(savedProduct3.getId());
        ProductOption productOption9 = ProductOption.builder()
                .productId(savedProduct3.getId())
                .color(testProductDTO3.getProductOptions().get(0).getColor())
                .size(testProductDTO3.getProductOptions().get(0).getSize())
                .stock(testProductDTO3.getProductOptions().get(0).getStock())
                .isDeleted(true)
                .build();


        ProductOption savedProductOption7 = productOptionRepository.save(productOption7);
        ProductOption savedProductOption8 = productOptionRepository.save(productOption8);
        ProductOption savedProductOption9 = productOptionRepository.save(productOption9);

        ProductOptionHistory productOptionHistory7 = ProductOptionHistory.toEntity(savedProductOption7);
        ProductOptionHistory productOptionHistory8 = ProductOptionHistory.toEntity(savedProductOption8);
        ProductOptionHistory productOptionHistory9 = ProductOptionHistory.toEntity(savedProductOption9);

        productOptionHistoryRepository.save(productOptionHistory7);
        productOptionHistoryRepository.save(productOptionHistory8);
        productOptionHistoryRepository.save(productOptionHistory9);

        // 6. 테스트 상품 4 생성 및 저장 (판매 중지된 상품)
        CreateProductDTO testProductDTO4 = createProductDTO("테스트 상품4", 10000, 10000, 8000, ProductStatus.SALES_STOP, ProductType.GREEN_TAG, createProductOptionDTOs);
        Product testProduct4 = testProductDTO4.toEntity(savedSeller.getId());
        Product savedProduct4 = productRepository.save(testProduct4);

        ProductOption productOption10 = testProductDTO2.getProductOptions().get(0).toEntity(savedProduct4.getId());
        ProductOption productOption11 = testProductDTO2.getProductOptions().get(1).toEntity(savedProduct4.getId());
        ProductOption productOption12 = ProductOption.builder()
                .productId(savedProduct2.getId())
                .color(testProductDTO2.getProductOptions().get(0).getColor())
                .size(testProductDTO2.getProductOptions().get(0).getSize())
                .stock(testProductDTO2.getProductOptions().get(0).getStock())
                .isDeleted(true)
                .build();

        ProductOption savedProductOption10 = productOptionRepository.save(productOption10);
        ProductOption savedProductOption11 = productOptionRepository.save(productOption11);
        ProductOption savedProductOption12 = productOptionRepository.save(productOption12);

        ProductOptionHistory productOptionHistory10 = ProductOptionHistory.toEntity(savedProductOption10);
        ProductOptionHistory productOptionHistory11 = ProductOptionHistory.toEntity(savedProductOption11);
        ProductOptionHistory productOptionHistory12 = ProductOptionHistory.toEntity(savedProductOption12);

        productOptionHistoryRepository.save(productOptionHistory10);
        productOptionHistoryRepository.save(productOptionHistory11);
        productOptionHistoryRepository.save(productOptionHistory12);


        // 7. 사용자(소비자) 생성
        User user = User.builder()
                .email("test@test.com")
                .password("testpassword")
                .name("testName")
                .userIdName("tsetUserIdName")
                .type(UserType.ROLE_CERTIFIED_USER)
                .phoneNumberPrefix("000-0000")
                .phoneNumberSuffix("0000")
                .isCertEmail(true)
                .isCertPhone(true)
                .certEmailAt(LocalDateTime.now())
                .certPhoneAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        User savedUser = userRepository.save(user);

        // 8. 포인트 지급 (10000)
        GreenLabelPoint greenLabelPoint = GreenLabelPoint.toEntity(savedUser.getId());
        GreenLabelPoint savedGreenLabelPoint = greenLabelPointRepository.save(greenLabelPoint);
        greenLabelPointRepository.updateGreenLabelPointByUserId(savedGreenLabelPoint.getUserId(), 10000L);

        // 9. 쿠폰 등록(혜택 구분 2개, 적용 타입 2개, 쿠폰 사용 범위 2개, 사용 가능 기준 금액 2개)
        // 9.1. 쿠폰 1: 10% 할인 쿠폰, 제약 조건 없음
        Coupon coupon1 = createCoupon("test1", BenefitType.PERCENTAGE, 10L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.UNLIMITED, null);
        // 9.2. 쿠폰 2: 10% 할인 쿠폰, 제약 조건 -> 브랜드(테스트마켓)
        Coupon coupon2 = createCoupon("tset2", BenefitType.PERCENTAGE, 10L, CouponProductType.ALL, CoverageType.BRAND, "테스트마켓", StandardType.UNLIMITED, null);
        // 9.3. 쿠폰 3: 10% 할인 쿠폰, 제약 조건 -> 브랜드(틀린이름)
        Coupon coupon3 = createCoupon("test3", BenefitType.PERCENTAGE, 10L, CouponProductType.ALL, CoverageType.BRAND, "틀린마켓이름", StandardType.UNLIMITED, null);
        // 9.4. 쿠폰 4: 10% 할인 쿠폰, 제약 조건 -> 브랜드(테스트마켓), 사용 기준 금액: 10000원
        Coupon coupon4 = createCoupon("test4", BenefitType.PERCENTAGE, 20L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.LIMIT, 10000L);
        // 9.5. 쿠폰 5: 10% 할인 쿠폰, 제약 조건 -> 브랜드(테스트마켓), 일반 상품, 사용 기준 금액: 10000원
        Coupon coupon5 = createCoupon("test5", BenefitType.PERCENTAGE, 10L, CouponProductType.BASIC, CoverageType.BRAND, "테스트마켓", StandardType.LIMIT, 10000L);
        // 9.6. 쿠폰 6: 10% 할인 쿠폰, 제약 조건 -> 에코 상품, 사용 기준 금액: 10000원
        Coupon coupon6 = createCoupon("test6", BenefitType.PERCENTAGE, 10L, CouponProductType.ECO_GREEN, CoverageType.ALL, null, StandardType.LIMIT, 10000L);
        // 9.7. 쿠폰 7: 5000원 할인 쿠폰, 제약 조건 없음
        Coupon coupon7 = createCoupon("test7", BenefitType.AMOUNT, 5000L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.UNLIMITED, null);
        // 9.8. 쿠폰 8: 10000원 할인 쿠폰, 제약 조건 -> 사용 가능 기준 금액 20000원
        Coupon coupon8 = createCoupon("test8", BenefitType.AMOUNT, 10000L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.LIMIT, 20000L);
        // 9.9. 쿠폰 9: 이미 사용한 쿠폰으로 처리 (제약 조건 X)
        Coupon coupon9 = createCoupon("test9", BenefitType.AMOUNT, 5000L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.UNLIMITED, null);
        // 9.10. 쿠폰 10: 만료된 쿠폰으로 처리 (제약 조건 X)
        Coupon coupon10 = createCoupon("test10", BenefitType.AMOUNT, 5000L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.UNLIMITED, null);
        // 9.11. 쿠폰 11: 지급 실패한 쿠폰으로 처리 (제약 조건 X)
        Coupon coupon11 = createCoupon("test11", BenefitType.AMOUNT, 5000L, CouponProductType.ALL, CoverageType.ALL, null, StandardType.UNLIMITED, null);

        Coupon savedCoupon1 = couponRepository.save(coupon1);
        Coupon savedCoupon2 = couponRepository.save(coupon2);
        Coupon savedCoupon3 = couponRepository.save(coupon3);
        Coupon savedCoupon4 = couponRepository.save(coupon4);
        Coupon savedCoupon5 = couponRepository.save(coupon5);
        Coupon savedCoupon6 = couponRepository.save(coupon6);
        Coupon savedCoupon7 = couponRepository.save(coupon7);
        Coupon savedCoupon8 = couponRepository.save(coupon8);
        Coupon savedCoupon9 = couponRepository.save(coupon9);
        Coupon savedCoupon10 = couponRepository.save(coupon10);
        Coupon savedCoupon11 = couponRepository.save(coupon11);

        // 10. 등록한 쿠폰을 이용해서 사용자 쿠폰 생성
        UserCoupon userCoupon1 = createUserCoupon(savedUser.getId(), savedCoupon1.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon2 = createUserCoupon(savedUser.getId(), savedCoupon2.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon3 = createUserCoupon(savedUser.getId(), savedCoupon3.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon4 = createUserCoupon(savedUser.getId(), savedCoupon4.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon5 = createUserCoupon(savedUser.getId(), savedCoupon5.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon6 = createUserCoupon(savedUser.getId(), savedCoupon6.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon7 = createUserCoupon(savedUser.getId(), savedCoupon7.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon8 = createUserCoupon(savedUser.getId(), savedCoupon8.getId(), false, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon9 = createUserCoupon(savedUser.getId(), savedCoupon9.getId(), true, null, UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon10 = createUserCoupon(savedUser.getId(), savedCoupon10.getId(), false, LocalDate.now().minusMonths(1), UserCouponStatus.ISSUE_SUCCESS);
        UserCoupon userCoupon11 = createUserCoupon(savedUser.getId(), savedCoupon11.getId(), false, null, UserCouponStatus.ISSUE_FAIL);

        userCouponRepository.save(userCoupon1);
        userCouponRepository.save(userCoupon2);
        userCouponRepository.save(userCoupon3);
        userCouponRepository.save(userCoupon4);
        userCouponRepository.save(userCoupon5);
        userCouponRepository.save(userCoupon6);
        userCouponRepository.save(userCoupon7);
        userCouponRepository.save(userCoupon8);
        userCouponRepository.save(userCoupon9);
        userCouponRepository.save(userCoupon10);
        userCouponRepository.save(userCoupon11);
    }
    private UserCoupon createUserCoupon(Long userId, Long couponId, Boolean isUsed, LocalDate expiredAt, UserCouponStatus userCouponStatus) {
        return UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .availableDownloadAt(LocalDate.now())
                .isDownload(true)
                .isUsed(isUsed)
                .expiredAt(expiredAt)
                .usedAt(isUsed ? LocalDateTime.now() : null)
                .status(userCouponStatus)
                .build();
    }
    private Coupon createCoupon(String code, BenefitType benefitType, Long benefitValue, CouponProductType productType, CoverageType useCoverageType, String brandName, StandardType useStandardType, Long useStandardValue) {
        return Coupon.builder()
                .code(code)
                .name("테스트용 쿠폰")
                .description("테스트용 쿠폰 설명")
                .benefitType(benefitType)
                .benefitValue(benefitValue)
                .productType(productType)
                .paymentTarget(PaymentTarget.ALL)
                .firstCount(0)
                .quantityIssued(0L)
                .issuedTimeType(IssuedTimeType.IMMEDIATE)
                .couponType(CouponType.PROVISION)
                .couponIssueType(CouponIssueType.ONETIME)
                .expireTimeType(ExpireTimeType.LIMIT)
                .expireTimeDays(7)
                .issueCoverageType(CoverageType.ALL)
                .issueCoverageSubCategoryName(null)
                .useCoverageType(useCoverageType)
                .useCoverageSubCategoryName(brandName)
                .useStandardType(useStandardType)
                .useStandardValue(useStandardValue)
                .issueConditionType(StandardType.UNLIMITED)
                .issueConditionValue(null)
                .periodType(PeriodType.UNSET)
                .periodStartAt(null)
                .periodEndAt(null)
                .numberOfPeriod(null)
                .autoManualType(AutoManualType.MANUAL)
                .loginAlarm(false)
                .smsAlarm(false)
                .emailAlarm(false)
                .kakaoAlarm(false)
                .statusType(CouponStatusType.ISSUED)
                .isDeleted(false)
                .build();





    }
    private CreateProductDTO createProductDTO(String name, Integer marketPrice, Integer appSalesPrice, Integer discountPrice, ProductStatus productStatus, ProductType productType, List<CreateProductOptionDTO> productOptionDTOs) {
        CreateProductDetailInfoDTO productDetail = new CreateProductDetailInfoDTO(
                "productType", "productMaterial", "productColor", "productSize", "dateOfManufacture",
                "washingPrecautions", "countryOfManufacture", "manufacturer", "importer", "quantityAssuranceStandards",
                "asManager", "contactNumber");

        CreateProductDeliveryTimeDTO productDeliveryTimeDTO = new CreateProductDeliveryTimeDTO(0, 0);

        CreateClaimInfoDTO createClaimInfoDTO = new CreateClaimInfoDTO();

        // Create list of product images and add an image URL
        List<String> productImages = new ArrayList<>();
        productImages.add("ImageURL");

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
                10,                     // salesChargePercent
                productDetail,            // productDetail
                productOptionDTOs,           // productOptions
                productDeliveryTimeDTO,   // deliveryTime
                createClaimInfoDTO        // claim
        );
    }

    private CreateProductOptionDTO createProductOptionDTO(Long stock) {
        return new CreateProductOptionDTO(null, "color", "size", stock);
    }

}
