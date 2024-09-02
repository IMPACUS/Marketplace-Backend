package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.CreateProductDeliveryTimeDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDeliveryTime;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductDeliveryTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductDeliveryTimeService {
    private final TemporaryProductDeliveryTimeRepository deliveryTimeRepository;

    /**
     * TemporaryProductDeliveryTime 저장 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void addTemporaryProductDeliveryTime(Long temporaryProductId, CreateProductDeliveryTimeDTO dto) {
        deliveryTimeRepository.save(dto.toTemporaryEntity(temporaryProductId));
    }

    /**
     * TemporaryProductDeliveryTime 수정 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void updateTemporaryProductDeliveryTime(Long temporaryProductId, CreateProductDeliveryTimeDTO dto) {
        int minDays = dto.getMinDays() == null ? 0 : dto.getMinDays();
        int maxDays = dto.getMaxDays() == null ? 0 : dto.getMaxDays();

        deliveryTimeRepository.updateTemporaryProductDeliveryTime(
                temporaryProductId, minDays, maxDays
        );
    }

    /**
     * temporaryProductId로 TemporaryProductDeliveryTime 를 조회하는 함수
     *
     * @param temporaryProductId
     * @return
     */
    public TemporaryProductDeliveryTime findTemporaryProductDeliveryTimeByTemporaryProductId(Long temporaryProductId) {
        return deliveryTimeRepository.findByTemporaryProductId(temporaryProductId).get(0);
    }
}
