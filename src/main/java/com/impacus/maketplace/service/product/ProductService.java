package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final ProductDetailInfoService productDetailInfoService;

    /**
     * 새로운 Product를 저장하는 함수
     *
     * @param productRequest
     * @return
     */
    public ProductDTO addProduct(ProductRequest productRequest) {
        // 1. productRequest 데이터 유효성 검사
        if (!validateProductRequest(productRequest)) {
            throw new CustomException(ErrorType.INVALID_PRODUCT);
        }

        // 2. 대표 이미지 저장

        // 3. 상풍 번호 생성
        String productNumber = StringUtils.getProductNumber();

        // 4. Product 저장
        Product product = Product.builder()
                .brandId(productRequest.getBrandId())
                .name(productRequest.getName())
                .productNumber(productNumber)
                .description(productRequest.getDescription())
                .categoryType(productRequest.getCategoryType())
                .deliveryFee(productRequest.getDeliveryFee())
                .refundFee(productRequest.getRefundFee())
                .marketPrice(productRequest.getMarketPrice())
                .appSalesPrice(productRequest.getAppSalesPrice())
                .discountPrice(productRequest.getDiscountPrice())
                .weight(productRequest.getWeight())
                .build();
        Product newProduct = saveProduct(product);

        // 5. Product option 저장
        productOptionService.addProductOption(newProduct.getId(), productRequest.getProductOptions());

        // 6. Product detail 저장
        productDetailInfoService.addProductDetailInfo(newProduct.getId(), productRequest.getProductDetail());

        return null;
    }

    /**
     * 전달받은 ProductRequest의 유효성 검사를 하는 함수
     *
     * @param productRequest
     * @return
     */
    public boolean validateProductRequest(ProductRequest productRequest) {
        String productName = productRequest.getName();
        DeliveryType deliveryType = productRequest.getDeliveryType();
        SubCategory subCategory = productRequest.getCategoryType();

        if (productName.length() > 50) {
            return false;
        } else if (deliveryType == DeliveryType.NONE) {
            return false;
        } else if (subCategory == SubCategory.NONE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * product를 저장하는 함수
     *
     * @param newProduct
     * @return
     */
    @Transactional
    public Product saveProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }
}
