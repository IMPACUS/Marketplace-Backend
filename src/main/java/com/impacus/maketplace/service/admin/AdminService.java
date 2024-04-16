package com.impacus.maketplace.service.admin;


import com.impacus.maketplace.dto.admin.AdminUserListDto;
import com.impacus.maketplace.repository.admin.AdminInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminInfoRepository adminInfoRepository;

    public List<AdminUserListDto> displayAdminList() {
        return adminInfoRepository.findAdminInfoList();
    }


}
