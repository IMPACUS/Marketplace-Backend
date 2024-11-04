package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.enumType.error.SellerErrorType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.dto.common.request.CouponIdsDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.dto.seller.response.*;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.seller.mapping.SellerMarketNameViewsMapping;
import com.impacus.maketplace.service.excel.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadSellerService {
    private final SellerRepository sellerRepository;
    private final ExcelService excelService;

    /**
     * 판매자 입점 현황을 조회하는 함수
     *
     * @return
     */
    public SellerEntryStatusDTO getEntryStatusStatistics() {
        try {
            return SellerEntryStatusDTO.builder()
                    .todayEntryCnt(getTodayCreatedSellerCnt())
                    .thisWeekEntryCnt(getThisWeekCreatedSellerCnt())
                    .approveEntryCnt(getApprovedSellerCnt())
                    .rejectEntryCnt(getRejectedSellerCnt())
                    .build();
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 오늘 입점 요청한 판매자 수를 찾는 함수
     *
     * @return
     */
    private Long getTodayCreatedSellerCnt() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime startOfDay = nowDate.atStartOfDay();
        LocalDateTime endOfDay = nowDate.atTime(LocalTime.MAX);

        return sellerRepository.countByCreateAtBetweenAndIsDeletedIsFalse(startOfDay, endOfDay);
    }

    /**
     * 이번 주 입점 요청한 판매자 수를 찾는 함수
     *
     * @return
     */
    private Long getThisWeekCreatedSellerCnt() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime startOfWeek = LocalDateTime.of(nowDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endOfWeek = LocalDateTime.of(nowDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);

        return sellerRepository.countByCreateAtBetweenAndIsDeletedIsFalse(startOfWeek, endOfWeek);
    }

    /**
     * 입점 승인된 판매자 수를 찾는 함수
     *
     * @return
     */
    private Long getApprovedSellerCnt() {
        return sellerRepository.countByEntryStatusAndIsDeletedIsFalse(EntryStatus.APPROVE);
    }

    /**
     * 입점 거절된 판매자 수를 찾는 함수
     *
     * @return
     */
    private Long getRejectedSellerCnt() {
        return sellerRepository.countByEntryStatusAndIsDeletedIsFalse(EntryStatus.REJECT);
    }

    /**
     * userId로 판매자 조회하는 함수
     *
     * @param userId
     * @return
     */
    public Seller findSellerByUserId(Long userId) {
        return sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(SellerErrorType.NOT_EXISTED_SELLER));
    }

    /**
     * sellerId로 판매자 조회하는 함수
     *
     * @param sellerId
     * @return
     */
    public Seller findSellerBySellerId(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(SellerErrorType.NOT_EXISTED_SELLER));
    }

    /**
     * 전체 판매자 입점 상태 리스트를 조회하는 함수
     *
     * @param startAt
     * @param endAt
     * @param entryStatus
     * @param pageable
     * @return
     */
    public Page<SimpleSellerEntryDTO> getSellerEntryList(LocalDate startAt,
                                                         LocalDate endAt,
                                                         EntryStatus[] entryStatus,
                                                         String brandName,
                                                         Pageable pageable) {
        try {
            return sellerRepository.findAllSellerWithEntry(startAt, endAt, pageable, entryStatus, brandName);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 입점 관련 상세 데이터 조회하는 함수
     *
     * @param userId
     * @return
     */
    public DetailedSellerEntryDTO getDetailedSellerEntry(Long userId) {
        try {
            // 1. 판매자 유효성 검사
            if (!sellerRepository.existsByUserId(userId)) {
                throw new CustomException(SellerErrorType.NOT_EXISTED_SELLER);
            }

            // 2. 데이터 조회
            return sellerRepository.findDetailedSellerEntry(userId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * Seller가 존재하는지 확인하는 함수
     *
     * @param sellerId
     */
    public void checkSellerExistenceById(Long sellerId) {
        if (!existsSellerBySellerId(sellerId)) {
            throw new CustomException(SellerErrorType.NOT_EXISTED_SELLER);
        }
    }

    /**
     * 존재하는 sellerId 인지 확인하는 함수
     *
     * @param sellerId
     * @return
     */
    public boolean existsSellerBySellerId(Long sellerId) {
        return sellerRepository.existsByIsDeletedFalseAndId(sellerId);
    }

    /**
     * 판매자 정보 관리 데이터 조회 함수
     *
     * @param userId
     * @return
     */
    public DetailedSellerDTO findSellerDetailInformation(Long userId) {
        try {
            return sellerRepository.findDetailedSellerInformationByUserId(userId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자에 등록된 전체 브랜드명을 반환하는 함수
     */
    public List<SubCategoryDetailDTO> findAllBrandName() {
        return sellerRepository.findAllBrandName();
    }

    /**
     *
     * @return
     */
    public List<SellerMarketNamesDTO> findSellerNames() {
        try {
            List<SellerMarketNameViewsMapping> mappings = sellerRepository.findMarketNames();

            return mappings.stream()
                    .map(SellerMarketNamesDTO::from)
                    .toList();
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 전체 판매자 목록 조회 함수 ([관리자] 판매자 관리 페이지)
     *
     * @param pageable
     * @param brandName
     * @param contactName
     * @param status
     * @return
     */
    public Page<SellerDTO> getSellers(
            Pageable pageable,
            String brandName,
            String contactName,
            UserStatus status,
            LocalDate startAt,
            LocalDate endAt
    ) {
        try {
            return sellerRepository.getSellers(
                    pageable,
                    brandName,
                    contactName,
                    status,
                    startAt,
                    endAt
            );
        } catch (Exception exception) {
            throw new CustomException(exception);
        }
    }

    /**
     * 판매자 정보 조회 함수 ([관리자] 판매자 관리 페이지)
     *
     * @param sellerId 조회할 판매자 아이디
     * @return
     */
    public SimpleSellerFromAdminDTO getSellerInformationFroWeb(Long sellerId) {
        try {
            return sellerRepository.getSellerInformation(sellerId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * userId로 sellerId 찾는 함수
     *
     * @param userId
     * @return
     */
    public Long findSellerIdByUserId(Long userId) {
        return sellerRepository.findSellerIdByUserId(userId);
    }

    /**
     * [앱] 상품 조회 페이지에서 판매자 아이디를 조회하는 함수
     * @param sellerId
     * @return
     */
    public AppSellerDTO getSellerInformationForApp(Long sellerId) {
        try {
            return sellerRepository.getSellerInformationForApp(sellerId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 목록 엑셀 생성 함수
     *
     * @return
     */
    public FileGenerationStatusIdDTO exportSellers(
            CouponIdsDTO dto
    ) {
        try {
            List<SellerDTO> dtos = sellerRepository.findSellersByIds(
                    dto
            );

            return excelService.generateExcel(dtos, SellerDTO.class);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
