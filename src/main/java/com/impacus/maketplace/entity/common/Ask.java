package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.AskType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EqualsAndHashCode(callSuper = false)
public class Ask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ask_id")
    private Long id;

    @ColumnDefault("0")
    @Column(name = "parent_id")
    private Long parentAskId;

    @Enumerated(EnumType.STRING)
    private AskType askType;

    private String title;

    private String contents;

    private boolean isAnswered;

    private boolean isDeleted;

}
