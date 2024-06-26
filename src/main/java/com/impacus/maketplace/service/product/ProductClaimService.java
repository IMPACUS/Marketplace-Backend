package com.impacus.maketplace.service.product;

import com.impacus.maketplace.repository.product.ProductClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductClaimService {
    private final ProductClaimRepository productClaimRepository;
}
