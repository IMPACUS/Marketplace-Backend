package com.impacus.maketplace.dto.system;

import com.impacus.maketplace.entity.system.SystemFooter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

@Data
public class SystemFooterOutputDto {
    private String imagePath;
    private String companyName;
    private String owner;
    private String businessNum;
    private String address;
    private String supportNum;
    private String email;
    private String copyright;
    private String termService;
    private String informationPolicy;
    private LocalTime startTime;
    private LocalTime endTime;
    private String week;
    private String informationNotice;

    public SystemFooterOutputDto(SystemFooter s) {
        this.companyName = s.getCompanyName();
        this.owner = s.getOwner();
        this.businessNum = s.getBusinessNum();
        this.address = s.getAddress();
        this.supportNum = s.getSupportNum();
        this.email = s.getEmail();
        this.copyright = s.getCopyright();
        this.termService = s.getTermService();
        this.informationPolicy = s.getInformationPolicy();
        this.startTime = s.getStartTime();
        this.endTime = s.getEndTime();
        this.week = s.getWeek();
        this.informationNotice = s.getInformationNotice();
    }


}
