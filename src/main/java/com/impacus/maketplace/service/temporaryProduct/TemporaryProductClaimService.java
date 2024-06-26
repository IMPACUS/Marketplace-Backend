package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.CreateClaimInfoDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductClaimInfo;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductClaimService {
    private final TemporaryProductClaimRepository temporaryProductClaimRepository;

    /**
     * TemporaryProductClaim 생성 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void addTemporaryProductClaim(Long temporaryProductId, CreateClaimInfoDTO dto) {
        TemporaryProductClaimInfo claimInfo = dto.toTemporaryEntity(temporaryProductId);
        temporaryProductClaimRepository.save(claimInfo);
    }

    /**
     * TemporaryProductClaim 수정 함수
     *
     * @param temporaryProductId
     * @param dto
     */
    public void updateTemporaryProductClaim(Long temporaryProductId, CreateClaimInfoDTO dto) {
        temporaryProductClaimRepository.updateTemporaryProductClaim(
                temporaryProductId,
                dto.getRecallInfo(),
                dto.getClaimCost(),
                dto.getClaimPolicyGuild(),
                dto.getClaimContactInfo()
        );
    }
}
