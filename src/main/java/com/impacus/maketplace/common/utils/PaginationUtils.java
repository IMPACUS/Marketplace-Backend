package com.impacus.maketplace.common.utils;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaginationUtils {

    public static <T> Slice<T> toSlice(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize() + 1), list.size());
        List<T> paginatedList = list.size() < start ? new ArrayList<>() : list.subList(start, end);

        boolean hasNext = false;
        if (paginatedList.size() > pageable.getPageSize()) {
            hasNext = true;
            paginatedList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(paginatedList, pageable, hasNext);
    }

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        long count = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize() + 1), list.size());
        List<T> paginatedList = list.size() < start ? new ArrayList<>() : list.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, count);
    }
}