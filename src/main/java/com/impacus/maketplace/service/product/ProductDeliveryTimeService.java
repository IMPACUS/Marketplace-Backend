package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.CreateProductDeliveryTimeDTO;
import com.impacus.maketplace.entity.product.ProductDeliveryTime;
import com.impacus.maketplace.repository.product.ProductDeliveryTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductDeliveryTimeService {
    private final ProductDeliveryTimeRepository deliveryTimeRepository;

    /**
     * ProductDeliveryTime 저장 함수
     *
     * @param productId
     * @param dto
     */
    public void addProductDeliveryTime(Long productId, CreateProductDeliveryTimeDTO dto) {
        deliveryTimeRepository.save(dto.toEntity(productId));
    }

    /**
     * ProductDeliveryTime 수정 함수
     *
     * @param productId
     * @param dto
     */
    public void updateProductDeliveryTime(Long productId, CreateProductDeliveryTimeDTO dto) {
        int minDays = dto.getMinDays() == null ? 0 : dto.getMinDays();
        int maxDays = dto.getMaxDays() == null ? 0 : dto.getMaxDays();

        deliveryTimeRepository.updateProductDeliveryTime(
                productId, minDays, maxDays
        );
    }

    /**
     * productId로 ProductDeliveryTime 조회 함수
     *
     * @param productId
     * @return
     */
    public ProductDeliveryTime findProductDeliveryTimeByProductId(Long productId) {
        List<ProductDeliveryTime> deliveryTimes = deliveryTimeRepository.findByProductId(productId);
        return deliveryTimes.isEmpty() ? null : deliveryTimes.get(0);
    }
}
