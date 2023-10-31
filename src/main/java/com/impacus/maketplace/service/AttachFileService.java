package com.impacus.maketplace.service;

import com.impacus.maketplace.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;
}
