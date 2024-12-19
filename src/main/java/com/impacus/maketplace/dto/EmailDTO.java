package com.impacus.maketplace.dto;

import com.impacus.maketplace.common.enumType.MailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private String receiveEmail;
    private String subject;
    private String authNo;
    private String mailType;
    private Map<String, String> emailContent;

    public EmailDTO(String receiveEmail, MailType mailType, Map<String, String> emailContent) {
        this.receiveEmail = receiveEmail;
        this.subject = mailType.getSubject();
        this.mailType = mailType.getTemplate();
        this.emailContent = emailContent;
    }

    public static EmailDTO toDTO(
            String receiveEmail,
            MailType mailType,
            Map<String, String> emailContent
    ) {
        return new EmailDTO(receiveEmail, mailType, emailContent);
    }
}