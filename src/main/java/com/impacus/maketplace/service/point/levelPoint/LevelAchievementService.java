package com.impacus.maketplace.service.point.levelPoint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.repository.point.levelPoint.LevelAchievementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelAchievementService {
    private final LevelAchievementRepository levelAchievementRepository;
}
