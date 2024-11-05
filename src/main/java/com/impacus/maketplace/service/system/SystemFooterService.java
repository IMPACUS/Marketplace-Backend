package com.impacus.maketplace.service.system;


import com.impacus.maketplace.dto.system.SystemFooterInputDto;
import com.impacus.maketplace.dto.system.SystemFooterOutputDto;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.system.SystemFooter;
import com.impacus.maketplace.repository.system.SystemFooterRepository;
import com.impacus.maketplace.service.AttachFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class SystemFooterService {
    private final SystemFooterRepository systemFooterRepository;
    private final AttachFileService attachFileService;
    private final String S3_DIRECTORY = "system";

    public void addAndUpdate(@Valid SystemFooterInputDto inputDto) {
        List<SystemFooter> all = systemFooterRepository.findAll();
        if (all.size() == 0) {
            AttachFile attachFile = attachFileService.uploadFileAndAddAttachFile(inputDto.getImage(), S3_DIRECTORY);
            systemFooterRepository.save(inputDto.toEntity(attachFile.getId()));
        } else {
            SystemFooter systemFooter = all.get(0);
            attachFileService.updateAttachFile(systemFooter.getImage(), inputDto.getImage(), S3_DIRECTORY);
            systemFooter.update(inputDto);
        }
    }

    public SystemFooterOutputDto find() {
        List<SystemFooter> all = systemFooterRepository.findAll();
        if (all.size() == 0) return null;

        SystemFooter systemFooter = all.get(0);
        SystemFooterOutputDto systemFooterOutputDto = new SystemFooterOutputDto(systemFooter);
        AttachFile attachFile = attachFileService.findAttachFileById(systemFooter.getImage());
        systemFooterOutputDto.setImagePath(attachFile.getAttachFileName());
        return systemFooterOutputDto;
    }

}
