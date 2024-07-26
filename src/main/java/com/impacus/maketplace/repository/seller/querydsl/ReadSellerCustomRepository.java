package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReadSellerCustomRepository {
    Page<SimpleSellerEntryDTO> findAllSellerWithEntry(LocalDate startAt, LocalDate endAt, Pageable pageable, EntryStatus[] entryStatus);

    DetailedSellerEntryDTO findDetailedSellerEntry(Long userId);

    DetailedSellerDTO findDetailedSellerInformationByUserId(Long userId);

    List<SubCategoryDetailDTO> findAllBrandName();
}
