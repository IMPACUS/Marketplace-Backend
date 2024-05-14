package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.entity.product.ProductDescription;
import com.impacus.maketplace.repository.product.ProductDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductDescriptionService {

    private final ProductDescriptionRepository productDescriptionRepository;

    /**
     * ProductDescription을 저장하는 함수
     *
     * @param productId
     * @param productRequest
     */
    @Transactional
    public ProductDescription addProductDescription(Long productId, CreateProductDTO productRequest) {
        ProductDescription newProductDescription = productRequest.toEntity(productId);

        return productDescriptionRepository.save(newProductDescription);
    }

    /**
     * 전달받은 productId로 ProductDescription를 찾는 함수
     *
     * @param productId
     * @return
     */
    public ProductDescription findProductDescriptionByProductId(Long productId) {
        List<ProductDescription> descriptions = productDescriptionRepository.findByProductId(productId);

        return descriptions.get(0);
    }

    /**
     * ProductDescrption 삭제하는 함수
     *
     * @param productDescription
     */
    @Transactional
    public void deleteProductDescription(ProductDescription productDescription) {
        productDescriptionRepository.delete(productDescription);
    }
}
