package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.ProductDetailInfoDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDetailInfo;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductDetailInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductDetailInfoService {
    private final TemporaryProductDetailInfoRepository temporaryProductDetailInfoRepository;

    /**
     * ProductDetailRequest를 TemporaryProductDetailInfo로 저장하는 함수
     *
     * @param temporaryProductId
     * @param productDetail
     */
    @Transactional
    public void addTemporaryProductDetailInfo(Long temporaryProductId, ProductDetailInfoDTO productDetail) {
        TemporaryProductDetailInfo newTemporaryProductDetailInfo = productDetail.toTemporaryEntity(temporaryProductId);
        temporaryProductDetailInfoRepository.save(newTemporaryProductDetailInfo);

    }

    /**
     * 전달받은 temporaryProductId로 TemporaryProductDetailInfo 을 찾는 함수
     *
     * @param temporaryProductId
     * @return
     */
    public TemporaryProductDetailInfo findTemporaryProductDetailInfoByProductId(Long temporaryProductId) {
        List<TemporaryProductDetailInfo> productDetailInfos = temporaryProductDetailInfoRepository.findByTemporaryProductId(temporaryProductId);

        return productDetailInfos.get(0);
    }
}
