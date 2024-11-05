package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.CreateProductOptionDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductOption;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductOptionService {

    private final TemporaryProductOptionRepository temporaryProductOptionRepository;


    /**
     * ProductOptionRequest 리스트를 TemporaryProductOption 객체로 모두 저장하는 함수
     *
     * @param temporaryProductId
     * @param productOptionRequestList
     * @return
     */
    @Transactional
    public void addTemporaryProductOption(Long temporaryProductId, List<CreateProductOptionDTO> productOptionRequestList) {
        productOptionRequestList.stream()
                .map(productOptionRequest -> productOptionRequest.toTemporaryEntity(temporaryProductId))
                .forEach(this::saveTemporaryProductOption);
    }

    /**
     * TemporaryProductOption 을 DB에 저장하는 함수
     *
     * @param newOption
     */
    @Transactional
    public TemporaryProductOption saveTemporaryProductOption(TemporaryProductOption newOption) {
        return temporaryProductOptionRepository.save(newOption);
    }

    /**
     * 전달받은 temporaryProductId로 TemporaryProductOption 을 찾는 함수
     *
     * @param temporaryProductId
     * @return
     */
    public List<TemporaryProductOption> findTemporaryProductOptionByProductId(Long temporaryProductId) {
        return temporaryProductOptionRepository.findByTemporaryProductId(temporaryProductId);
    }

    /**
     * temporaryProductId 와 연결된 모든 TemporaryProductOption 을 삭제하는 함수
     *
     * @param temporaryProductId
     */
    @Transactional
    public void deleteAllTemporaryProductionOptionByTemporaryProductId(Long temporaryProductId) {
        temporaryProductOptionRepository.deleteByTemporaryProductId(temporaryProductId);
    }

    /**
     * temporaryProductId에 연결된 모든 TemporaryProductOption을 삭제하고 다시 등록하는 함수
     *
     * @param temporaryProductId
     * @param productOptionRequestList
     */
    @Transactional
    public void initializeTemporaryProductionOption(Long temporaryProductId, List<CreateProductOptionDTO> productOptionRequestList) {
        // 1. 생성되어 있는 TemporaryProductOption을 모두 삭제
        deleteAllTemporaryProductionOptionByTemporaryProductId(temporaryProductId);

        // 2. 생성
        addTemporaryProductOption(temporaryProductId, productOptionRequestList);
    }
}
