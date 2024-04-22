package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {
    private final SellerRepository sellerRepository;

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
}
