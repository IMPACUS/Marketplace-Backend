package com.impacus.maketplace.entity.common;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.common.InfoType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "base_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "base_info_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private InfoType infoType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;
}
