package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.constants.PointConstants;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.dto.product.dto.ProductTypeDTO;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.api.GreenLabelPointApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointApiServiceImpl implements GreenLabelPointApiService {
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final ProductRepository productRepository;

    /**
     * 상품 구매 포인트를 지급하는 함수
     *
     * @param userId     포인트 지급 대상 사용자 ID
     * @param productIds 포인트 지급 상품
     * @param paymentId  PaymentEvent.id
     * @return
     */
    @Override
    @Transactional
    public boolean payGreenLabelPointForProductPurchase(Long userId, List<Long> productIds, String paymentId) {
        List<ProductTypeDTO> data = productRepository.findProductForPoint(productIds);
        Long totalPoint = 0L;
        for (ProductTypeDTO productTypeDTO : data) {
            switch (productTypeDTO.getType()) {
                case GENERAL:
                    totalPoint += PointConstants.PURCHASE_GENERAL_PRODUCT;
                    break;
                case GREEN_TAG:
                    totalPoint += PointConstants.PURCHASE_GREEN_TAG_PRODUCT;
                    break;
                default:
                    break;
            }
        }

        return greenLabelPointAllocationService.payGreenLabelPoint(
                userId,
                PointType.PURCHASE_PRODUCT,
                totalPoint,
                paymentId
        );
    }
}
