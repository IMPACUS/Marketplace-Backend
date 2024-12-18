package com.impacus.maketplace.service.product.bundleDelivery;

import com.impacus.maketplace.common.enumType.error.BundleDeliveryGroupErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDTO;
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
            Long sellerId = getSellerIdByUserType(userId, dto.getSellerId());

            // 1. 유효성 검사
            readSellerService.checkSellerExistenceById(sellerId);
            if (bundleDeliveryGroupRepository.existsByNameAndSellerIdAndIsDeletedFalse(dto.getName(), sellerId)) {
                throw new CustomException(BundleDeliveryGroupErrorType.DUPLICATED_BUNDLE_DELIVERY_GROUP_NAME);
            }

            // 2. 묶음 배송 그룹 생성
            BundleDeliveryGroup bundleDeliveryGroup = dto.toEntity(sellerId);

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
            Long sellerId = getSellerIdByUserType(userId, dto.getSellerId());

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

    private Long getSellerIdByUserType(Long userId, Long sellerId) {
        UserType userType = SecurityUtils.getCurrentUserType();
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            return readSellerService.findSellerIdByUserId(userId);
        } else {
            if (sellerId == null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA,
                        String.format("요청 권한: %s, sellerId는 null일 수 없습니다.", userType.name())
                );
            }

            return sellerId;
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
            Long sellerId,
            String keyword,
            Pageable pageable,
            String sortBy,
            String direction
    ) {
        try {
            Long resolvedSellerId = getSellerIdByUserType(userId, sellerId);

            return bundleDeliveryGroupRepository.findDetailBundleDeliveryGroups(
                    resolvedSellerId,
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

    /**
     * 묶음 배송 그룹에 속한 상품 삭제
     * - 상품은 개별 배송으로 변경
     * - 묶음 배송 그룹 아이디 null로 변경
     * - 묶음 배송 상품 적용일은 null로 변경
     *
     * @param groupId
     * @param productId
     * @return
     */
    @Transactional
    public void deleteProductFromBundleGroup(Long userId, Long groupId, Long productId) {
        try {
            // 1. 업데이트
            long result = bundleDeliveryGroupRepository.deleteProductFromBundleGroup(
                    groupId,
                    productId
            );

            // 2. 업데이트된 쿼리가 존재하지 않는 경우,
            if (result == 0) {
                throw new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * (상품 등록/수정용) 묶음 배송 그룹 목록 조회
     * - 사용 상태인 데이터만 조회
     *
     * @param userId
     * @param sellerId
     * @param keyword
     * @param pageable
     * @return
     */
    public Page<BundleDeliveryGroupDTO> findBundleDeliveryGroupsBySeller(
            Long userId,
            Long sellerId,
            String keyword,
            Pageable pageable
    ) {
        try {
            // 1. sellerId 조회
            UserType userType = SecurityUtils.getCurrentUserType();
            if (userType == UserType.ROLE_APPROVED_SELLER) {
                sellerId = readSellerService.findSellerIdByUserId(userId);
            } else {
                if (sellerId == null) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "seller-id가 null일 수 없습니다.");
                }
            }

            // 2. 묶음 배송 그룹 조회
            return bundleDeliveryGroupRepository.findBundleDeliveryGroupsBySeller(
                    sellerId, keyword, pageable
            );
        } catch (Exception ex) {
            throw new CustomException(ex);
        }

    }

    public BundleDeliveryGroupDetailDTO getBundleDeliveryGroup(Long userId, Long groupId) {
        UserType userType = SecurityUtils.getCurrentUserType();

        // 묶음 배송 그룹 조회
        BundleDeliveryGroup group = bundleDeliveryGroupRepository.findByIdAndIsDeletedFalse(groupId)
                .orElseThrow(() -> new CustomException(BundleDeliveryGroupErrorType.NOT_EXISTED_BUNDLE_DELIVERY_GROUP));

        // 유효성 검사
        if (userType.equals(UserType.ROLE_APPROVED_SELLER)) {
            Long sellerId = readSellerService.findSellerIdByUserId(userId);
            if (sellerId != group.getSellerId()) {
                throw new CustomException(BundleDeliveryGroupErrorType.NOT_EXISTED_BUNDLE_DELIVERY_GROUP, "판매자가 등록한 묶음 배송 그룹이 아닙니다.");
            }
        }

        return BundleDeliveryGroupDetailDTO.toDTO(group);

    }
}
