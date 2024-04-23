package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.seller.request.SellerChargePercentageRequest;
import com.impacus.maketplace.dto.seller.request.SellerRequest;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {
    private static final int FILE_LIMIT = 5242880;
    private static final int LOGO_IMAGE_LIMIT = 187500;
    private static final String LOGO_IMAGE_DIRECTORY = "logoImage";
    private static final String COPY_FILE_DIRECTORY = "copyFile";

    private final SellerRepository sellerRepository;
    private final SellerBusinessInfoService sellerBusinessInfoService;
    private final SellerAdjustmentInfoService sellerAdjustmentInfoService;
    private final AttachFileService attachFileService;
    private final UserService userService;

    /**
     * Seller를 저장하는 함수
     *
     * @param seller
     * @return
     */
    @Transactional
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    /**
     * 판매자를 생성하는 함수
     *
     * @param sellerRequest
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     * @return
     */
    @Transactional
    public SimpleSellerDTO addSeller(SellerRequest sellerRequest,
                                     MultipartFile logoImage,
                                     MultipartFile businessRegistrationImage,
                                     MultipartFile mailOrderBusinessReportImage,
                                     MultipartFile bankBookImage) {
        try {
            String email = sellerRequest.getEmail();
            String password = sellerRequest.getPassword();

            // 1. 이메일 유효성 검사
            if (userService.existUserByEmail(email)) {
                throw new CustomException(ErrorType.DUPLICATED_EMAIL);
            }

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(ErrorType.INVALID_PASSWORD);
            }

            // 3. 상세 데이터 유효성 검사
            if (sellerRequest.getBusinessType() == BusinessType.SIMPLIFIED_TAXABLE_PERSON) {
                if (mailOrderBusinessReportImage != null || sellerRequest.getMailOrderBusinessReportNumber() != null) {
                    throw new CustomException(ErrorType.INVALID_REQUEST_DATA, "간이 과세자인 경우, 통신판매업신고증관련 값이 null 이여야 합니다.");
                }
            } else {
                if (mailOrderBusinessReportImage == null || sellerRequest.getMailOrderBusinessReportNumber() == null) {
                    throw new CustomException(ErrorType.INVALID_REQUEST_DATA, "간이 과세자가 아닌 경우, 통신판매업신고증관련 값이 null 이면 안됩니다.");
                }
            }
            validateFileLimit(logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);

            // 4. 로고 이미지, 사업자 등록증 사본, 통신판매업신고증사본, 통장 사본 저장
            AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(logoImage, LOGO_IMAGE_DIRECTORY);
            AttachFile businessRegistrationFile = attachFileService.uploadFileAndAddAttachFile(businessRegistrationImage, COPY_FILE_DIRECTORY);
            AttachFile mailOrderBusinessReportFile = mailOrderBusinessReportImage == null ? null : attachFileService.uploadFileAndAddAttachFile(mailOrderBusinessReportImage, COPY_FILE_DIRECTORY);
            AttachFile bankBookFile = attachFileService.uploadFileAndAddAttachFile(bankBookImage, COPY_FILE_DIRECTORY);

            // 5. User 저장
            User user = new User(
                    email,
                    userService.encodePassword(password),
                    email,
                    sellerRequest.getContactNumber(),
                    UserType.ROLE_SELLER);
            userService.saveUser(user);

            // 6. Seller 저장
            Seller seller = saveSeller(sellerRequest.toSellerEntity(user.getId(), logoImageFile.getId()));
            Long sellerId = seller.getId();

            // 7. SellerBusinessInfo 저장
            SellerBusinessInfo sellerBusinessInfo = sellerRequest.toSellerBusinessInfoEntity(sellerId,
                    businessRegistrationFile.getId(),
                    mailOrderBusinessReportFile == null ? null : mailOrderBusinessReportFile.getId());
            sellerBusinessInfoService.saveSellerBusinessInfo(sellerBusinessInfo);

            // 8. SellerAdjustmentInfo 저장
            SellerAdjustmentInfo adjustmentInfo = sellerRequest.toSellerAdjustmentInfo(sellerId, bankBookFile.getId());
            sellerAdjustmentInfoService.saveSellerAdjustmentInfo(adjustmentInfo);

            return SimpleSellerDTO.toDTO(user);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    private void validateFileLimit(MultipartFile logoImage,
                                   MultipartFile businessRegistrationImage,
                                   MultipartFile mailOrderBusinessReportImage,
                                   MultipartFile bankBookImage) {
        if (logoImage.getSize() > LOGO_IMAGE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "로고 이미지 크기가 제한을 넘었습니다.");
        }

        if (businessRegistrationImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "사업자 등록증 사본의 이미지 크기가 제한을 넘었습니다.");
        }

        if (mailOrderBusinessReportImage != null && mailOrderBusinessReportImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "통신판매업신고증 사본의 크기가 제한을 넘었습니다.");
        }

        if (bankBookImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "통장 사본의 이미지 크기가 제한을 넘었습니다.");
        }

    }

    /**
     * 판매자 입점 현황을 조회하는 함수
     *
     * @return
     */
    public SellerEntryStatusDTO getEntryStatusStatistics() {
        return SellerEntryStatusDTO.builder()
                .todayEntryCnt(getTodayCreatedSellerCnt())
                .thisWeekEntryCnt(getThisWeekCreatedSellerCnt())
                .approveEntryCnt(getApprovedSellerCnt())
                .rejectEntryCnt(getApprovedSellerCnt())
                .build();
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
     * 판매자 입점 상태를 변경하는 함수
     *
     * @return
     */
    public SimpleSellerDTO changeEntryStatus(Long sellerId, SellerChargePercentageRequest feePercentageRequest) {
        return new SimpleSellerDTO();
    }
}
