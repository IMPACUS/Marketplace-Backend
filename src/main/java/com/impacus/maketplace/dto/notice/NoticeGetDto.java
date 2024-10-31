package com.impacus.maketplace.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeGetDto {
    private String title;
    private String content;
    private String imagePath;
}
