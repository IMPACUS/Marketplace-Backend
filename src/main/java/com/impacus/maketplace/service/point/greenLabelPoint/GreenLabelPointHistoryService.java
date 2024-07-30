package com.impacus.maketplace.service.point.greenLabelPoint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointHistoryService {
    private final GreenLabelPointHistoryRepository greenLabelPointHistoryRepository;
}
