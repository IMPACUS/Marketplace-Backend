package com.impacus.maketplace.repository.product.bundleDelivery.querydsl;

import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BundleDeliveryGroupCustomRepository {

    Page<BundleDeliveryGroupDetailDTO> findDetailBundleDeliveryGroups(
            Long sellerId,
            String keyword,
            Pageable pageable,
            String sortBy,
            String direction
    );
}
