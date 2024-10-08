package com.impacus.maketplace.common.utils;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationUtils {

    /**
     * List를 페이지네이션을 위한 Slice로 변환
     *
     * @param <T>      리스트의 요소 타입
     * @param list     Slice로 변환할 대상 리스트
     * @param pageable 페이지 번호, 크기 등의 페이지네이션 정보
     * @return 페이지 크기에 맞게 잘린 Slice 객체
     */
    public static <T> Slice<T> toSlice(List<T> list, Pageable pageable) {
        boolean hasNext = false;
        if (list.size() > pageable.getPageSize()) {
            hasNext = true;
            list.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(list, pageable, hasNext);
    }

    /**
     * List를 페이지네이션을 위한 Page로 변환
     *
     * @param <T>      리스트의 요소 타입
     * @param list     Page로 변환할 대상 리스트
     * @param pageable 페이지 번호, 크기 등의 페이지네이션 정보
     * @return 페이지 크기에 맞게 잘린 Page 객체
     */
    public static <T> Page<T> toPage(List<T> list, Pageable pageable, long count) {
        return new PageImpl<>(list, pageable, count);
    }
}