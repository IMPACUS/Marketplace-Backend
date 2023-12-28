package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE attach_file SET is_deleted = true WHERE attach_file_id = ?")
@Where(clause = "is_deleted = false")
public class AttachFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attach_file_id")
    private Long id;

    private String attachFileName; // 첨부파일 이름

    private Long attachFileSize; // 첨부파일 용량

    private String originalFileName; // 원본 이름

    private String attachFileExt; // 첨부파일 확장자
    
    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부
}

