package com.impacus.maketplace.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private String to;
    private String subject;
    private String message;

    @Data
    public static class Response {
        private String code;
    }
}
