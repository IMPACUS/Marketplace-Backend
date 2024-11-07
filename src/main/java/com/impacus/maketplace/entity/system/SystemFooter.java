package com.impacus.maketplace.entity.system;

import com.impacus.maketplace.dto.system.SystemFooterInputDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(name = "system_footer")
@Entity
@NoArgsConstructor
@Getter
public class SystemFooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attach_file_id", nullable = false)
    private Long image;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String businessNum;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String supportNum;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String copyright;

    @Column(nullable = false)
    private String termService;

    @Column(nullable = false)
    private String informationPolicy;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String week;

    @Column(nullable = false, columnDefinition = "text")
    private String informationNotice;

    public SystemFooter(SystemFooterInputDto s, Long attachFileId) {
        this.image = attachFileId;
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

    public void update(SystemFooterInputDto s) {
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
