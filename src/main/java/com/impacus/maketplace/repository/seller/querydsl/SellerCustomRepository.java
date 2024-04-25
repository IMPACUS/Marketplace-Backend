package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SellerCustomRepository {
    Page<SimpleSellerEntryDTO> findAllSellerWithEntry(LocalDate startAt, LocalDate endAt, Pageable pageable, EntryStatus[] entryStatus);

    DetailedSellerEntryDTO findDetailedSellerEntry(Long userId);
}
