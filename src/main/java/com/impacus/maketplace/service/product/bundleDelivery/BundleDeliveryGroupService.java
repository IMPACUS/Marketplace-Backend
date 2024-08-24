package com.impacus.maketplace.service.product.bundleDelivery;

import com.impacus.maketplace.common.enumType.error.BundleDeliveryGroupErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDetailDTO;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupProductDTO;
import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import com.impacus.maketplace.repository.product.bundleDelivery.BundleDeliveryGroupRepository;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param userId 생성하는 판매자 아이디
     * @param dto      묶음 배송 그룹 데이터
     */
    @Transactional
    public void addBundleDeliveryGroup(Long userId, CreateBundleDeliveryGroupDTO dto) {
        try {
            Long sellerId = readSellerService.findSellerIdByUserId(userId);

            // 1. 유효성 검사
            readSellerService.checkSellerExistenceById(sellerId);
            if (bundleDeliveryGroupRepository.existsByNameAndSellerIdAndIsDeletedFalse(dto.getName(), sellerId)) {
                throw new CustomException(BundleDeliveryGroupErrorType.DUPLICATED_BUNDLE_DELIVERY_GROUP_NAME);
            }

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
    public void updateBundleDeliveryGroup(Long userId, Long groupId, CreateBundleDeliveryGroupDTO dto) {
        try {
            Long sellerId = readSellerService.findSellerIdByUserId(userId);

            // 1. 유효성 검사
            Optional<BundleDeliveryGroup> groupOptional = bundleDeliveryGroupRepository.findById(groupId);
            if (groupOptional.isEmpty()) {
                throw new CustomException(BundleDeliveryGroupErrorType.NOT_EXISTED_BUNDLE_DELIVERY_GROUP);
            }
            BundleDeliveryGroup bundleDeliveryGroup = groupOptional.get();
            if (!bundleDeliveryGroup.getName().equals(dto.getName()) &&
                    bundleDeliveryGroupRepository.existsByNameAndSellerIdAndIsDeletedFalse(dto.getName(), sellerId)) {
                throw new CustomException(BundleDeliveryGroupErrorType.DUPLICATED_BUNDLE_DELIVERY_GROUP_NAME);
            }

            // 2. 수정
            bundleDeliveryGroup.updateBundleDeliveryGroup(dto);

            bundleDeliveryGroupRepository.save(bundleDeliveryGroup);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 묶음 배송 그룹 삭제
     *
     * @param groupId
     */
    @Transactional
    public void deleteBundleDeliveryGroup(Long groupId) {
        try {
            bundleDeliveryGroupRepository.updateIsDeleteTrueById(groupId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 묶음 배송 그룹 리스트 함수
     *
     * @param userId
     * @param keyword
     * @param pageable
     * @return
     */
    public Page<BundleDeliveryGroupDetailDTO> findDetailBundleDeliveryGroups(
            Long userId,
            String keyword,
            Pageable pageable,
            String sortBy,
            String direction
    ) {
        try {
            Long sellerId = readSellerService.findSellerIdByUserId(userId);

            return bundleDeliveryGroupRepository.findDetailBundleDeliveryGroups(
                    sellerId,
                    keyword,
                    pageable,
                    sortBy,
                    direction
            );
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 묶음 배송 그룹 페이지에서 상품 조회 함수
     *
     * @param groupId
     * @param keyword
     * @param pageable
     * @return
     */
    public Page<BundleDeliveryGroupProductDTO> findProductsByDetailBundleDeliveryGroup(
            Long groupId, String keyword, Pageable pageable
    ) {
        try {
            return bundleDeliveryGroupRepository.findProductsByDetailBundleDeliveryGroup(
                    groupId,
                    keyword,
                    pageable
            );
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
