package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseTimeEntity;
import com.impacus.maketplace.common.BaseUserEntity;
import com.impacus.maketplace.common.enumType.AskType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ask extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ask_id")
    private Long id;

    @Column(name = "parent_id")
    @ColumnDefault("0")
    private Long parentAskId;

    @Enumerated(EnumType.STRING)
    private AskType askType;

    private String title;

    private String contents;

    private boolean isAnswered;

    private boolean isDeleted;



}
