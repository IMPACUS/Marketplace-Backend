package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDescription;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductDescriptionService {

    private final TemporaryProductDescriptionRepository temporaryProductDescriptionRepository;

    /**
     * ProductDescription 을 저장하는 함수
     *
     * @param temporaryProductId
     * @param productRequest
     */
    @Transactional
    public TemporaryProductDescription addTemporaryProductDescription(Long temporaryProductId, CreateProductDTO productRequest) {
        TemporaryProductDescription newDescription = productRequest.toTemporaryDescriptionEntity(temporaryProductId);

        return temporaryProductDescriptionRepository.save(newDescription);
    }

    /**
     * 전달받은 temporaryProductId로 TemporaryProductDescription 를 찾는 함수
     *
     * @param temporaryProductId
     * @return
     */
    public TemporaryProductDescription findProductDescriptionByTemporaryProductId(Long temporaryProductId) {
        List<TemporaryProductDescription> descriptions = temporaryProductDescriptionRepository.findByTemporaryProductId(temporaryProductId);

        return descriptions.get(0);
    }

    /**
     * TemporaryProductDescription 삭제하는 함수
     *
     * @param temporaryProductDescription
     */
    @Transactional
    public void deleteTemporaryProductDescription(TemporaryProductDescription temporaryProductDescription) {
        temporaryProductDescriptionRepository.delete(temporaryProductDescription);
    }
}
