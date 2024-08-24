package com.impacus.maketplace.service.product.bundleDelivery;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import com.impacus.maketplace.repository.product.bundleDelivery.BundleDeliveryGroupRepository;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BundleDeliveryGroupService {
    private final BundleDeliveryGroupRepository bundleDeliveryGroupRepository;
    private final ReadSellerService readSellerService;

    /**
     * 묶음 배송 그룹 생성
     *
     * @param sellerId 생성하는 판매자 아이디
     * @param dto      묶음 배송 그룹 데이터
     */
    @Transactional
    public void addBundleDeliveryGroup(Long sellerId, CreateBundleDeliveryGroupDTO dto) {
        try {
            // 1. 유효성 검사
            readSellerService.checkSellerExistenceById(sellerId);

            // 2. 묶음 배송 그룹 생성
            String groupNumber = StringUtils.getRandomUniqueNumber();
            BundleDeliveryGroup bundleDeliveryGroup = dto.toEntity(sellerId, groupNumber);

            bundleDeliveryGroupRepository.save(bundleDeliveryGroup);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
