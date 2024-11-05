package com.impacus.maketplace.dto.system;

import com.impacus.maketplace.entity.system.SystemFooter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

@Data
public class SystemFooterInputDto {
    @NotNull(message = "이미지 등록은 필수입니다.")
    private MultipartFile image;

    @NotBlank(message = "상호명을 입력해주세요.")
    private String companyName;

    @NotBlank(message = "대표를 입력해주세요.")
    private String owner;

    @NotBlank(message = "사업자등록번호를 입력해주세요.")
    private String businessNum;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "고객센터 전화번호를 입력해주세요.")
    private String supportNum;

    @NotBlank(message = "종합문의를 입력해주세요.")
    private String email;

    @NotBlank(message = "Copyright를 입력해주세요.")
    private String copyright;

    @NotBlank(message = "이용약관을 입력해주세요.")
    private String termService;

    @NotBlank(message = "개인정보처리방침을 입력해주세요.")
    private String informationPolicy;

    @NotNull(message = "시작시간은 null 안됩니다.")
    private LocalTime startTime;

    @NotNull(message = "종료시간은 null 안됩니다.")
    private LocalTime endTime;

    @NotBlank(message = "요일을 입력해주세요.")
    private String week;

    @NotBlank(message = "정보제공고시를 입력해주세요.")
    private String informationNotice;

    public SystemFooter toEntity(Long attachFileId) {
        return new SystemFooter(this, attachFileId);
    }
}
