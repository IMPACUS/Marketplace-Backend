package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attach_file_id")
    private Long id;

    private String attachFileName; // 첨부파일 이름

    private int attachFileSize; // 첨부파일 용량

    private String originalFileName; // 원본 이름

    private String attachFileExt; // 첨부파일 확장자
}

