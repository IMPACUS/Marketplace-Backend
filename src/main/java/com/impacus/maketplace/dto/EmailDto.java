package com.impacus.maketplace.dto;

import com.impacus.maketplace.common.enumType.MailType;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private String receiveEmail;
    private String subject;
    private String authNo;
    private String mailType;

}
