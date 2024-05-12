package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.ProductDetailInfoDTO;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.repository.product.ProductDetailInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public void addProductDetailInfo(Long productId, ProductDetailInfoDTO productDetail) {
        ProductDetailInfo newProductDetailInfo = productDetail.toEntity(productId);
        productDetailInfoRepository.save(newProductDetailInfo);

    }

    /**
     * 전달받은 productId로 ProductDetailInfo 을 찾는 함수
     *
     * @param productId
     * @return
     */
    public ProductDetailInfo findProductDetailInfoByProductId(Long productId) {
        List<ProductDetailInfo> productDetailInfos = productDetailInfoRepository.findByProductId(productId);

        return productDetailInfos.get(0);
    }
}
