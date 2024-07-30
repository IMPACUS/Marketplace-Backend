package com.impacus.maketplace.service.point.levelPoint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.repository.point.levelPoint.LevelPointHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelPointMasterService {
    private final LevelPointHistoryRepository levelPointHistoryRepository;
}
