package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.seller.request.ChangeSellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.request.CreateSellerDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final EmailService emailService;

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
     * @param sellerDTO
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     * @return
     */
    @Transactional
    public SimpleSellerDTO addSeller(CreateSellerDTO sellerDTO,
                                     MultipartFile logoImage,
                                     MultipartFile businessRegistrationImage,
                                     MultipartFile mailOrderBusinessReportImage,
                                     MultipartFile bankBookImage) {
        try {
            String email = sellerDTO.getEmail();
            String password = sellerDTO.getPassword();

            // 1. 유효성 검사
            validateSellerInput(
                    sellerDTO,
                    logoImage,
                    businessRegistrationImage,
                    mailOrderBusinessReportImage,
                    bankBookImage);

            // 2. 로고 이미지, 사업자 등록증 사본, 통신판매업신고증사본, 통장 사본 저장
            AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(logoImage, LOGO_IMAGE_DIRECTORY);
            AttachFile businessRegistrationFile = attachFileService.uploadFileAndAddAttachFile(businessRegistrationImage, COPY_FILE_DIRECTORY);
            AttachFile mailOrderBusinessReportFile = mailOrderBusinessReportImage == null ? null : attachFileService.uploadFileAndAddAttachFile(mailOrderBusinessReportImage, COPY_FILE_DIRECTORY);
            AttachFile bankBookFile = attachFileService.uploadFileAndAddAttachFile(bankBookImage, COPY_FILE_DIRECTORY);

            // 3. User 저장
            User user = new User(
                    email,
                    userService.encodePassword(password),
                    email,
                    sellerDTO.getContactNumber(),
                    UserType.ROLE_UNAPPROVED_SELLER);
            userService.saveUser(user);

            // 4. Seller 저장
            Seller seller = saveSeller(sellerDTO.toSellerEntity(user.getId(), logoImageFile.getId()));
            Long sellerId = seller.getId();

            // 5. SellerBusinessInfo 저장
            SellerBusinessInfo sellerBusinessInfo = sellerDTO.toSellerBusinessInfoEntity(sellerId,
                    businessRegistrationFile.getId(),
                    mailOrderBusinessReportFile == null ? null : mailOrderBusinessReportFile.getId());
            sellerBusinessInfoService.saveSellerBusinessInfo(sellerBusinessInfo);

            // 6. SellerAdjustmentInfo 저장
            SellerAdjustmentInfo adjustmentInfo = sellerDTO.toSellerAdjustmentInfo(sellerId, bankBookFile.getId());
            sellerAdjustmentInfoService.saveSellerAdjustmentInfo(adjustmentInfo);

            return SimpleSellerDTO.toDTO(user.getId(), seller);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * CreateSellerDTO 유효성 검사 하는 함수
     *
     * @param sellerDTO
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     */
    private void validateSellerInput(
            CreateSellerDTO sellerDTO,
            MultipartFile logoImage,
            MultipartFile businessRegistrationImage,
            MultipartFile mailOrderBusinessReportImage,
            MultipartFile bankBookImage) {
        String email = sellerDTO.getEmail();
        String password = sellerDTO.getPassword();

        // 1. 이메일 유효성 검사
        if (userService.existUserByEmail(email)) {
            throw new CustomException(CommonErrorType.DUPLICATED_EMAIL);
        }

        // 2. 비밃번호 유효성 검사
        if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
            throw new CustomException(CommonErrorType.INVALID_PASSWORD);
        }

        // 3. 상세 데이터 유효성 검사
        validateBusinessType(sellerDTO.getBusinessType(), mailOrderBusinessReportImage, sellerDTO.getMailOrderBusinessReportNumber());
        validateFileLimit(logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);
    }

    /**
     * Seller의 BusinessType 유효성 검사하는 함수
     */
    private void validateBusinessType(
            BusinessType businessType,
            MultipartFile mailOrderBusinessReportImage,
            String mailOrderBusinessReportNumber
    ) {
        if (businessType == BusinessType.SIMPLIFIED_TAXABLE_PERSON) {
            if (mailOrderBusinessReportImage != null || mailOrderBusinessReportNumber != null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "간이 과세자인 경우, 통신판매업신고증관련 값이 null 이여야 합니다.");
            }
        } else {
            if (mailOrderBusinessReportImage == null || mailOrderBusinessReportNumber == null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "간이 과세자가 아닌 경우, 통신판매업신고증관련 값이 null 이면 안됩니다.");
            }
        }
    }

    private void validateFileLimit(MultipartFile logoImage,
                                   MultipartFile businessRegistrationImage,
                                   MultipartFile mailOrderBusinessReportImage,
                                   MultipartFile bankBookImage) {
        if (logoImage.getSize() > LOGO_IMAGE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "로고 이미지 크기가 제한을 넘었습니다.");
        }

        if (businessRegistrationImage.getSize() > FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "사업자 등록증 사본의 이미지 크기가 제한을 넘었습니다.");
        }

        if (mailOrderBusinessReportImage != null && mailOrderBusinessReportImage.getSize() > FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "통신판매업신고증 사본의 크기가 제한을 넘었습니다.");
        }

        if (bankBookImage.getSize() > FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "통장 사본의 이미지 크기가 제한을 넘었습니다.");
        }

    }

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
     * 판매자 입점 상태를 변경하는 함수
     *
     * @param sellerDTO
     * @return
     */
    @Transactional
    public Boolean changeEntryStatus(ChangeSellerEntryStatusDTO sellerDTO) {
        try {
            Long userId = sellerDTO.getUserId();
            EntryStatus entryStatus = sellerDTO.getEntryStatus();
            Integer charge = sellerDTO.getCharge();

            // 1. 유효성 검사
            User user = userService.findUserById(userId);
            Seller seller = findSellerByUserId(userId);
            validateSellerEntry(entryStatus, charge);

            // 2. 입점 상태 및 판매자 수수료 저장
            sellerRepository.updateSellerEntryStatusAndChargePercent(
                    seller.getId(),
                    entryStatus,
                    charge == null ? 0 : charge
            );

            EmailDto emailDto = EmailDto.builder()
                    .subject("입점 결과 메일 입니다.")
                    .receiveEmail(user.getEmail())
                    .build();

            // 3. Role 변경
            if (entryStatus == EntryStatus.APPROVE) {
                userService.updateUserType(userId, UserType.ROLE_APPROVED_SELLER);
                emailService.sendMail(emailDto, MailType.SELLER_APPROVE);
            } else {
                userService.updateUserType(userId, UserType.ROLE_UNAPPROVED_SELLER);
                emailService.sendMail(emailDto, MailType.SELLER_REJECT);
            }

            sellerRepository.save(seller);

            return true;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * Seller 입점 상태 변경 데이터 유효성 검사하는 함수
     */
    private void validateSellerEntry(EntryStatus entryStatus, Integer charge) {
        if (entryStatus == EntryStatus.APPROVE) {
            if (charge == null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "charge는 null이 될 수 없습니다.");
            }
        } else if (entryStatus == EntryStatus.REJECT) {
            if (charge != null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "charge는 null 이여야 합니다.");
            }
        } else {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "entryStatus 데이터가 올바르지 않습니다.");
        }
    }

    /**
     * userId로 판매자 조회하는 함수
     *
     * @param userId
     * @return
     */
    public Seller findSellerByUserId(Long userId) {
        return sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_SELLER));
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
                                                         Pageable pageable) {
        try {
            return sellerRepository.findAllSellerWithEntry(startAt, endAt, pageable, entryStatus);
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
                throw new CustomException(CommonErrorType.NOT_EXISTED_SELLER);
            }

            // 2. 데이터 조회
            return sellerRepository.findDetailedSellerEntry(userId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
