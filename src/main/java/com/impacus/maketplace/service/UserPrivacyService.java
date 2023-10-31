package com.impacus.maketplace.service;

import com.impacus.maketplace.repository.UserPrivacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPrivacyService {

    private final UserPrivacyRepository userPrivacyRepository;
}
