package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.ProductDetailInfoRequest;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.repository.ProductDetailInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductDetailInfoService {
    private final ProductDetailInfoRepository productDetailInfoRepository;

    /**
     * ProductDetailRequest를 ProductDetailInfo로 저장하는 함수
     *
     * @param productId
     * @param productDetail
     */
    public void addProductDetailInfo(Long productId, ProductDetailInfoRequest productDetail) {
        ProductDetailInfo productDetailInfo = new ProductDetailInfo(productId, productDetail);
        saveProductDetailInfo(productDetailInfo);

    }

    /**
     * ProductDetailInfo를 DB에 저장하는 함수
     *
     * @param newProductDetailInfo
     */
    @Transactional
    public void saveProductDetailInfo(ProductDetailInfo newProductDetailInfo) {
        productDetailInfoRepository.save(newProductDetailInfo);
    }
}
