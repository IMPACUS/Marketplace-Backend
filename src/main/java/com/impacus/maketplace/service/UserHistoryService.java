package com.impacus.maketplace.service;

import com.impacus.maketplace.repository.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;
}
