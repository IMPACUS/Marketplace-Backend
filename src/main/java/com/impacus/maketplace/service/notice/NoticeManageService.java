package com.impacus.maketplace.service.notice;

import com.impacus.maketplace.common.enumType.error.NoticeErrorType;
import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.notice.*;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.notice.Notice;
import com.impacus.maketplace.repository.notice.NoticeRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeManageService {
    private final NoticeRepository noticeRepository;
    private final AttachFileService attachFileService;
    private final String S3_DIRECTORY = "noticeImage";

    public void timeValidation(LocalDate startDate, LocalDate endDate, int startTime, int endTime) {
        LocalDateTime startDateTime = startDate.atTime(startTime, 0);
        LocalDateTime endDateTime = endDate.atTime(endTime, 0);

        if (startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime))
            throw new CustomException(HttpStatus.BAD_REQUEST, NoticeErrorType.DATE_TIME_ERROR);
    }

    public void add(NoticeManageSaveDto n) {
        MultipartFile image = n.getImage();
        AttachFile attachFile = null;
        if (image != null)
            attachFile = attachFileService.uploadFileAndAddAttachFile(image, S3_DIRECTORY);

        noticeRepository.save(n.toEntity(attachFile == null ? null : attachFile.getId()));
    }

    public NoticeManageGetDto find(Long noticeId) {
        return noticeRepository.findManage(noticeId);
    }

    public void update(NoticeManageUpdateDto n) {
        Optional<Notice> optional = noticeRepository.findById(n.getNoticeId());
        if (optional.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, NoticeErrorType.NO_NOTICE_ID);

        Notice notice = optional.get();
        MultipartFile image = n.getImage();
        Long attachFileId = notice.getAttachFileId();

        if (image == null && attachFileId != null) { // 이미지 있다가 지워졌을 경우
            attachFileService.deleteAttachFile(attachFileId);
            attachFileId = null;
        } else if (image != null) {
            if (attachFileId != null) // 기존 이미지 수정
                attachFileService.updateAttachFile(notice.getAttachFileId(), n.getImage(), S3_DIRECTORY);
            else // 이미지 없었다가 생성할 때
                attachFileId = attachFileService.uploadFileAndAddAttachFile(image, S3_DIRECTORY).getId();
        }
        notice.update(n, attachFileId);
    }

    @Transactional(readOnly = true)
    public NoticeManageListDto findList(NoticeManageSearchDto noticeManageSearchDto, Pageable pageable) {
        List<NoticeManageDataDto> list = noticeRepository.findManageList(noticeManageSearchDto, pageable);
        List<Notice> all = noticeRepository.findAll();

        // 통신 비용을 줄이고자 자바에서 필터링
        long count = all.stream().filter(a -> {
            String search = noticeManageSearchDto.getSearch();
            NoticeType type = noticeManageSearchDto.getType();

            if (StringUtils.hasText(search) && type != null)
                return a.getTitle().equals(search) && a.getType().equals(type);
            else if (StringUtils.hasText(search))
                return a.getTitle().equals(search);
            else if (type != null)
                return a.getType().equals(type);
            else
                return true;
        }).count();

        NoticeManageListDto noticeManageListDto = new NoticeManageListDto();
        noticeManageListDto.setData(new PageImpl<>(list, pageable, count));
        noticeManageListDto.setAllCount(all.size());
        noticeManageListDto.setNoticeCount((int) all.stream().filter(a -> a.getType().equals(NoticeType.NOTICE)).count());
        noticeManageListDto.setEventCount((int) all.stream().filter(a -> a.getType().equals(NoticeType.EVENT)).count());
        return noticeManageListDto;
    }

    public void updateStatus(List<Long> ids, NoticeStatus status) {
        List<Notice> list = noticeRepository.findAllById(ids);
        for (Notice n : list) n.setStatus(status);
    }

    public void delete(List<Long> ids) {
        List<Notice> list = noticeRepository.findAllById(ids);
        for (Notice n : list) {
            if (n.getAttachFileId() != null) attachFileService.deleteAttachFile(n.getAttachFileId());
        }
        noticeRepository.deleteAllById(ids);
    }


}
