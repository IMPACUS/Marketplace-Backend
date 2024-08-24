package com.impacus.maketplace.service.product.bundleDelivery;

import com.impacus.maketplace.common.enumType.error.BundleDeliveryGroupErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import com.impacus.maketplace.repository.product.bundleDelivery.BundleDeliveryGroupRepository;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * 묶음 배송 그룹 수정
     *
     * @param groupId 수정할 묶음 배송 그룹 아이디
     * @param dto     묶음 배송 그룹 데이터
     */
    @Transactional
    public void updateBundleDeliveryGroup(Long groupId, CreateBundleDeliveryGroupDTO dto) {
        try {
            // 1. 유효성 검사
            Optional<BundleDeliveryGroup> groupOptional = bundleDeliveryGroupRepository.findById(groupId);
            if (!groupOptional.isPresent()) {
                throw new CustomException(BundleDeliveryGroupErrorType.NOT_EXISTED_BUNDLE_DELIVERY_GROUP);
            }

            // 2. 수정
            BundleDeliveryGroup bundleDeliveryGroup = groupOptional.get();
            bundleDeliveryGroup.updateBundleDeliveryGroup(dto);

            bundleDeliveryGroupRepository.save(bundleDeliveryGroup);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
