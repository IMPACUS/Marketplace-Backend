package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.ProductOptionRequest;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;


    /**
     * ProductOptionRequest 리스트를 ProductOption 객체로 모두 저장하는 함수
     *
     * @param productId
     * @param productOptionRequestList
     * @return
     */
    @Transactional
    public void addProductOption(Long productId, List<ProductOptionRequest> productOptionRequestList) {
        productOptionRequestList.stream()
                .map(productOptionRequest -> ProductOption.builder()
                        .productId(productId)
                        .color(productOptionRequest.getColor())
                        .size(productOptionRequest.getSize())
                        .stock(productOptionRequest.getStock())
                        .build())
                .forEach(this::saveProductOption);
    }

    /**
     * ProductOption을 DB에 저장하는 함수
     *
     * @param newProductOption
     */
    @Transactional
    public ProductOption saveProductOption(ProductOption newProductOption) {
        return productOptionRepository.save(newProductOption);
    }


}
