package com.impacus.maketplace.service.point.greenLabelPoint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointAllocationService {
    private final GreenLabelPointAllocationRepository greenLabelPointAllocationRepository;
}
