package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFileGroup extends BaseEntity { // TODO 논의 후, soft delete 추가
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attach_file_group_id")
    private Long id;

    private Long attachFileId; // AttachFile 객체 id

    private Long referencedId; // AttachFile을 참조하는 id

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ReferencedEntityType referencedEntity;
}
