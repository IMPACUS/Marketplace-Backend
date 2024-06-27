package com.impacus.maketplace.service.product;

import com.impacus.maketplace.dto.product.request.CreateClaimInfoDTO;
import com.impacus.maketplace.entity.product.ProductClaimInfo;
import com.impacus.maketplace.repository.product.ProductClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductClaimService {
    private final ProductClaimRepository productClaimRepository;


    /**
     * ProductClaimInfo 생성 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void addProductClaimInfo(Long temporaryProductId, CreateClaimInfoDTO dto) {
        ProductClaimInfo claimInfo = dto.toEntity(temporaryProductId);
        productClaimRepository.save(claimInfo);
    }

    /**
     * TemporaryProductClaim 수정 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void updateProductClaimInfo(Long temporaryProductId, CreateClaimInfoDTO dto) {
        productClaimRepository.updateProductClaimInfo(
                temporaryProductId,
                dto.getRecallInfo(),
                dto.getClaimCost(),
                dto.getClaimPolicyGuild(),
                dto.getClaimContactInfo()
        );
    }
}
