package com.impacus.maketplace.service.notice;

import com.impacus.maketplace.common.enumType.error.NoticeErrorType;
import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.notice.NoticeManageDataDto;
import com.impacus.maketplace.dto.notice.NoticeManageGetDto;
import com.impacus.maketplace.dto.notice.NoticeManageSaveDto;
import com.impacus.maketplace.dto.notice.NoticeManageSearchDto;
import com.impacus.maketplace.entity.notice.NoticeManage;
import com.impacus.maketplace.repository.notice.NoticeManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeManageService {
    private final NoticeManageRepository noticeManageRepository;

    public void timeValidation(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        if (startTime != null) startDateTime = startDateTime.toLocalDate().atTime(startTime);
        if (endTime != null) endDateTime = endDateTime.toLocalDate().atTime(endTime);

        if (startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime))
            throw new CustomException(HttpStatus.BAD_REQUEST, NoticeErrorType.DATE_TIME_ERROR);
    }

    public void add(NoticeManageSaveDto noticeManageSaveDto) {
        noticeManageRepository.save(noticeManageSaveDto.toEntity());
    }

    @Transactional(readOnly = true)
    public NoticeManageGetDto findList(NoticeManageSearchDto noticeManageSearchDto, Pageable pageable) {
        List<NoticeManageDataDto> list = noticeManageRepository.findList(noticeManageSearchDto, pageable);
        List<NoticeManage> all = noticeManageRepository.findAll();

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

        NoticeManageGetDto noticeManageGetDto = new NoticeManageGetDto();
        noticeManageGetDto.setData(new PageImpl<>(list, pageable, count));
        noticeManageGetDto.setAllCount(all.size());
        noticeManageGetDto.setNoticeCount((int) all.stream().filter(a -> a.getType().equals(NoticeType.NOTICE)).count());
        noticeManageGetDto.setEventCount((int) all.stream().filter(a -> a.getType().equals(NoticeType.EVENT)).count());
        return noticeManageGetDto;
    }

    public void updateStatus(List<Long> ids, NoticeStatus status) {
        List<NoticeManage> list = noticeManageRepository.findAllById(ids);
        for (NoticeManage n : list) n.setStatus(status);
    }

    public void delete(List<Long> ids) {
        noticeManageRepository.deleteAllById(ids);
    }
}
